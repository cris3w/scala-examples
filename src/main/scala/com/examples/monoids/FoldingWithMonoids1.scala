package com.examples.monoids


// examples of "learning Scalaz" tutorial

object FoldingWithMonoids1 extends App {


  def sum1[A](xs: List[A], m: Monoid[A]): A =
    xs.foldLeft(m.zero)(m.op)

  def sum2[A](xs: List[A])(implicit m: Monoid[A]): A =
    xs.foldLeft(m.zero)(m.op)

  def sum3[A: Monoid](xs: List[A]): A = {
    val m = implicitly[Monoid[A]]
    xs.foldLeft(m.zero)(m.op)
  }


  import com.examples.monoids.PlusMonoid._

  println(sum1(List(1, 2, 3, 4), IntMonoid)) // res: 10

  println(sum2(List(1, 2, 3, 4))) // res: 10

  println(sum3(List(1, 2, 3, 4))(IntMonoid)) // res: 10

}