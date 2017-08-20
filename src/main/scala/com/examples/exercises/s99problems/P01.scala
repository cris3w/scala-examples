package com.examples.exercises.s99problems


// P01 - Find the last element of a list
object P01 extends App {


  // O(n), where n is the size of the list xs
  def last1[T](xs: List[T]): T = xs.last


  // O(n), where n is the size of the list xs
  def last2[T](xs: List[T]): T = xs match {
    case List() => throw new Error("last of empty list")
    case List(x) => x
    case _ :: ys => last2(ys)
  }


  val list = List(5, 3, 7, 2)
  println(last1(list)) // 2
  println(last2(list)) // 2

}
