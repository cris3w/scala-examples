package com.examples.ScalaZ.Monads.MonadTransformers

import scala.concurrent.Future
import scalaz.OptionT
import scalaz.Scalaz._


// example of 47deg's blog:
// https://www.47deg.com/blog/fp-for-the-average-joe-part-2-scalaz-monad-transformers/


case class Country(zipCode: Option[String])

case class Address(id: String, country: Option[Country])

case class Person(name: String, address: Option[Address])


object OptionTExample {

  // 1) only with Option

  def getCountryCode1A(maybePerson : Option[Person]) : Option[String] =
    maybePerson flatMap { person =>
      person.address flatMap { address =>
        address.country flatMap { country =>
          country.zipCode
        }
      }
    }

  def getCountryCode1B(maybePerson : Option[Person]) : Option[String] =
    for {
      person <- maybePerson
      address <- person.address
      country <- address.country
      code <- country.zipCode
    } yield code


  // 2) calls a remote service and returns a Future

  def findPerson(id : String) : Future[Option[Person]] = ???

  def findCountry(addressId : String) : Future[Option[Country]] = ???

  // we can’t use flatMap because the nested expression doesn't match return type of the expression it is contained within
  def getCountryCode2A(personId : String): Future[Option[Option[Future[Option[Option[String]]]]]] =
    findPerson(personId) map { maybePerson =>
      maybePerson map { person =>
        person.address map { address =>
          findCountry(address.id) map { maybeCountry =>
            maybeCountry map { country =>
              country.zipCode
            }
          }
        }
      }
    }

  def getCountryCode2B(personId : String): Future[Option[String]] =
    findPerson(personId) flatMap { case Some(Person(_, Some(address))) =>
      findCountry(address.id) map { case Some(Country(code)) =>
        code
      }
    }

  def getCountryCode2C(personId : String): Future[Option[String]] =
    for {
      maybePerson <- findPerson(personId)
      person <- Future.successful {
        maybePerson getOrElse (throw new NoSuchElementException("..."))
      }
      address <- Future.successful {
        person.address getOrElse (throw new NoSuchElementException("..."))
      }
      maybeCountry <- findCountry(address.id)
      country <- Future.successful {
        maybeCountry getOrElse (throw new NoSuchElementException("..."))
      }
    } yield country.zipCode


  // 3) using monad transformer OptionT

  def getCountryCode3A(personId : String): Future[Option[String]] =
    (for {
      person <- OptionT(findPerson(personId))
      address <- OptionT(Future.successful(person.address))
      country <- OptionT(findCountry(address.id))
      code <- OptionT(Future.successful(country.zipCode))
    } yield code).run


  // to help with the syntax
  object ? {

    type Result[A] = OptionT[Future, A]

    def <~[A] (v: Future[Option[A]]) : Result[A] = OptionT(v)

    def <~[A] (v: Option[A]) : Result[A] = OptionT(Future.successful(v))

    def <~[A] (v: A) : Result[A] = v.point[Result]
  }

  def getCountryCode3B(personId : String): Future[Option[String]] =
    (for {
      person <- ? <~ findPerson(personId)
      address <- ? <~ person.address
      country <- ? <~ findCountry(address.id)
      code <- ? <~ country.zipCode
    } yield code).run

}
