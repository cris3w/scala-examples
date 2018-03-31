package com.examples.Monoids


// examples of "learning Scalaz" tutorial

object FoldingWithMonoids2 extends App {


  // FoldLeft operation

  object FoldLeftList {
    def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B) =
      xs.foldLeft(b)(f)
  }

  def sum4[A: MyMonoid](xs: List[A]): A = {
    val m = implicitly[MyMonoid[A]]
    FoldLeftList.foldLeft(xs, m.zero, m.op)
  }


  import com.examples.Monoids.PlusMonoid._

  println(sum4(List(1, 2, 3, 4))) // res: 10

  println(sum4(List("a", "b", "c"))) // res: abc

}
