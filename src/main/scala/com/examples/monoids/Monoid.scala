package com.examples.monoids


// Monoid typeclass

trait Monoid[A] {
  def op(a1: A, a2: A): A
  def zero: A
}

object PlusMonoid {

  implicit object IntMonoid extends Monoid[Int] {
    def op(a1: Int, a2: Int): Int = a1 + a2
    val zero = 0
  }

  implicit object StringMonoid extends Monoid[String] {
    def op(a1: String, a2: String): String = a1 + a2
    val zero = ""
  }

  implicit object BooleanMonoid extends Monoid[Boolean] {
    def op(a1: Boolean, a2: Boolean): Boolean = a1 && a2
    val zero = true
  }

  class ListMonoid[A] extends Monoid[List[A]] {
    def op(a1: List[A], a2: List[A]): List[A] = a1 ++ a2
    val zero = Nil
  }

  implicit object ListMonoid extends ListMonoid

}
