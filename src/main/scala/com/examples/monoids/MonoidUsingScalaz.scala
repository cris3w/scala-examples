package com.examples.monoids

import scalaz._
import Scalaz._


// examples of "learning Scalaz" tutorial

object MonoidUsingScalaz extends App {

  // using Scalaz

  implicit def optionMonoid[A: Semigroup]: Monoid[Option[A]] = new Monoid[Option[A]] {

    def zero: Option[A] = None

    def append(f1: Option[A], f2: => Option[A]): Option[A] =
      (f1, f2) match {
        case (Some(a1), Some(a2)) => Some(Semigroup[A].append(a1, a2))
        case (Some(_), None) => f1
        case (None, Some(_)) => f2
        case (None, None) => None
      }

  }

  println((none: Option[String]) |+| "andy".some) // Some(andy)

  println((None: Option[String]) |+| Some("andy")) // Some(andy)

}
