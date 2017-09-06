package com.examples.ScalaZ.Monoids


// Monoid typeclass

trait MyMonoid[A] {
  def op(a1: A, a2: A): A
  def zero: A
}

object PlusMonoid {

  implicit object IntMonoid extends MyMonoid[Int] {
    def op(a1: Int, a2: Int): Int = a1 + a2
    val zero = 0
  }

  implicit object StringMonoid extends MyMonoid[String] {
    def op(a1: String, a2: String): String = a1 + a2
    val zero = ""
  }

  implicit object BooleanMonoid extends MyMonoid[Boolean] {
    def op(a1: Boolean, a2: Boolean): Boolean = a1 && a2
    val zero = true
  }

  class ListMonoid[A] extends MyMonoid[List[A]] {
    def op(a1: List[A], a2: List[A]): List[A] = a1 ++ a2
    val zero = Nil
  }

  implicit object ListMonoid extends ListMonoid

}
