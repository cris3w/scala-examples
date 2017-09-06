package com.examples.TypeClasses


// example of davegurnell's gist:
// https://gist.github.com/davegurnell/a614c67e8d52c113d36d


object TypeClassDemo {

  // === Library ===

  // Type class:

  trait HtmlWriter[A] {
    def write(value: A): String
  }

  // Type class interface:

  def toHtml[A](value: A)(implicit ev: HtmlWriter[A]): String = ev.write(value)

  // Constructor for type class instances:
    // This isn't a core part of the type class pattern,
    // but it helps keep the instance definitions short.

  object HtmlWriter {
    def apply[A](f: A => String) = new HtmlWriter[A] {
      def write(value: A): String = f(value)
    }
  }

  // Type class instances:
    // Libraries typically ship with a set of default instances for common data types

  implicit val intWriter: HtmlWriter[Int] =
    HtmlWriter((value: Int) => value.toString)

  implicit val stringWriter: HtmlWriter[String] =
    HtmlWriter((value: String) => value.replaceAll("<", "&lt;").replaceAll(">", "&gt;"))


  // === Aplication ===

  // Data types:

  case class Email(address: String)
  case class Person(name: String, email: Email, age: Int)

  // Additional type class instances:

  implicit val emailWriter: HtmlWriter[Email] =
    HtmlWriter((email: Email) => email.address.replaceAll("@", " at "))

  implicit val personWriter: HtmlWriter[Person] =
    HtmlWriter { (person: Person) =>
        toHtml(person.name) + " " +
          toHtml(person.email) + " " +
          toHtml(person.age)
    }

  // Using the type class:
    // via one of its interface methods

  // canonical use case -- we specify the data we want to write
  // and allow the compiler to fill in the implicit parameters automatically:

  toHtml(Person("Dave", Email("dave@example.com"), 36))

  // specifying the implicit parameters explicitly, bypassing the implicit lookup system:

  toHtml(123)(intWriter)

  // using the instances directly:

  stringWriter.write("Dave <dave@example.com>")

}
