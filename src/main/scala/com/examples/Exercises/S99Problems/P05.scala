package com.examples.Exercises.S99Problems


// P05 - Reverse a list
object P05 extends App {


  // O(n*n), where n is the size of the list xs
  def reverse1[T](xs: List[T]): List[T] = xs.reverse


  // O(n*n), where n is the size of the list xs
  def reverse2[T](xs: List[T]): List[T] = xs match {
    case List() => xs
    case y :: ys => reverse2(ys) ++ List(y)
  }


  val list = List(5, 3, 7, 2)
  println(reverse1(list)) // List(2, 7, 3, 5)
  println(reverse2(list)) // List(2, 7, 3, 5)

}
