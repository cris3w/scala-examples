package com.examples.Monoids

import scalaz.Monoid


object SumMonoid {

  implicit def sumInt: Monoid[Int] = new Monoid[Int] {
    override def zero: Int = 0
    override def append(f1: Int, f2: => Int): Int = f1 + f2
  }

  implicit def sumBigDecimal: Monoid[BigDecimal] = new Monoid[BigDecimal] {
    override def zero: BigDecimal = BigDecimal(0)
    override def append(f1: BigDecimal, f2: => BigDecimal): BigDecimal = f1 + f2
  }

  implicit def sumListInt[A]: Monoid[List[A]] = new Monoid[List[A]] {
    override def zero: List[A] = List.empty[A]
    override def append(f1: List[A], f2: => List[A]): List[A] = f1 ++ f2
  }

  implicit class SumOps[A](iterable: Iterable[A]) {

    def total[B](amountFn: A => B)(implicit m: Monoid[B]): B =
      iterable.foldLeft(m.zero)((total, elem) => m.append(total, amountFn(elem)))
  }

}

object MyFoldingWithScalazMonoid extends App {

  import SumMonoid._

  case class Data(numberI: Int, numberBD: BigDecimal, list: List[String])

  val data = List(Data(1, 2.2, List("a", "b")), Data(1, 1.5, List("c", "d")))

  println(data.total(_.numberI)) // res: 2
  println(data.total(_.numberBD)) // res: 3.7
  println(data.total(_.list)) // res: List(a, b, c, d)

}
