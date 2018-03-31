package com.examples.Monoids


// examples of "learning Scalaz" tutorial

object FoldingWithMonoids3 extends App {


  // FoldLeft typeclass

  trait FoldLeft[F[_]] {
    def foldLeft[A, B](xs: F[A], b: B, f: (B, A) => B): B
  }

  object FoldLeft {
    implicit val FoldLeftList: FoldLeft[List] = new FoldLeft[List] {
      def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B) =
        xs.foldLeft(b)(f)
    }
  }

  def sum5[M[_]: FoldLeft, A: MyMonoid](xs: M[A]): A = {
    val m = implicitly[MyMonoid[A]]
    val fl = implicitly[FoldLeft[M]]
    fl.foldLeft(xs, m.zero, m.op)
  }


  import com.examples.Monoids.PlusMonoid._

  println(sum5(List(1, 2, 3, 4))) // res: 10

  println(sum5(List("a", "b", "c"))) // res: abc

}
