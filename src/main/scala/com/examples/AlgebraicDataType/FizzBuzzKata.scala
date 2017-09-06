package com.examples.AlgebraicDataType


// example of Monsanto's blog:
// http://engineering.monsanto.com/2016/01/11/algebraic-data-types/


sealed abstract class FizzBuzzADT(n: Int) {
  override def toString: String = n.toString
}

case class Fizz(n: Int) extends FizzBuzzADT(n) {
  override val toString = "Fizz"
}

case class Buzz(n: Int) extends FizzBuzzADT(n) {
  override val toString = "Buzz"
}

case class FizzBuzz(n: Int) extends FizzBuzzADT(n) {
  override val toString = "FizzBuzz"
}

case class JustInt(n: Int) extends FizzBuzzADT(n)


object FizzBuzzADT {

  def apply(n: Int): FizzBuzzADT = n match {
    case _ if n % 3 == 0 && n % 5 == 0 => FizzBuzz(n)
    case _ if n % 3 == 0 => Fizz(n)
    case _ if n % 5 == 0 => Buzz(n)
    case _ => JustInt(n)
  }
}


object FizzBuzzKata extends App {

  Stream(1 to 100: _*).map(FizzBuzzADT(_)).foreach(println)
}
