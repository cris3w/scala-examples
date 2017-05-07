package com.examples.monoids


// examples of "learning Scalaz" tutorial

object MethodInjection extends App {

  import com.examples.monoids.PlusMonoid._


  // sum two using Monoid

  // 1)

  def plus[A: Monoid](a1: A, a2: A): A = implicitly[Monoid[A]].op(a1, a2)

  println(plus(3, 4)) // res: 7

  // 2)

  trait MonoidOp[A] {
    val F: Monoid[A]
    val value: A
    def |+|(a2: A): A = F.op(value, a2)
  }

  implicit def toMonoidOp[A: Monoid](a: A): MonoidOp[A] = new MonoidOp[A] {
    val F: Monoid[A] = implicitly[Monoid[A]]
    val value: A = a
  }

  println(3 |+| 4) // res: 7

  println("a" |+| "b") // res: ab

}
