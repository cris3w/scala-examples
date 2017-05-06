package com.examples.monoids


// examples of "learning Scalaz" tutorial

class FoldingWithMonoids extends App {


  def sum[A](xs: List[A], m: Monoid[A]): A =
    xs.foldLeft(m.zero)(m.op)

  def sum[A](xs: List[A])(implicit m: Monoid[A]): A =
    xs.foldLeft(m.zero)(m.op)

  def sum[A: Monoid](xs: List[A]): A = {
    val m = implicitly[Monoid[A]]
    xs.foldLeft(m.zero)(m.op)
  }


  // FoldLeft operation

  object FoldLeftList {
    def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B) =
      xs.foldLeft(b)(f)
  }

  def sum[A: Monoid](xs: List[A]): A = {
    val m = implicitly[Monoid[A]]
    FoldLeftList.foldLeft(xs, m.zero, m.op)
  }


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

  def sum[M[_]: FoldLeft, A: Monoid](xs: M[A]): A = {
    val m = implicitly[Monoid[A]]
    val fl = implicitly[FoldLeft[M]]
    fl.foldLeft(xs, m.zero, m.op)
  }

}
