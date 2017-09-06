package com.examples.ScalaZ.Monoids


// examples of "learning Scalaz" tutorial

object FoldingWithMonoids1 extends App {


  def sum1[A](xs: List[A], m: MyMonoid[A]): A =
    xs.foldLeft(m.zero)(m.op)

  def sum2[A](xs: List[A])(implicit m: MyMonoid[A]): A =
    xs.foldLeft(m.zero)(m.op)

  def sum3[A: MyMonoid](xs: List[A]): A = {
    val m = implicitly[MyMonoid[A]]
    xs.foldLeft(m.zero)(m.op)
  }


  import com.examples.ScalaZ.Monoids.PlusMonoid._

  println(sum1(List(1, 2, 3, 4), IntMonoid)) // res: 10

  println(sum2(List(1, 2, 3, 4))) // res: 10

  println(sum3(List(1, 2, 3, 4))(IntMonoid)) // res: 10

}
