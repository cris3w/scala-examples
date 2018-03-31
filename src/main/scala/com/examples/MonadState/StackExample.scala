package com.examples.MonadState

import scalaz.State
import scalaz.State._


// examples of "learning Scalaz" tutorial:
// http://eed3si9n.com/learning-scalaz/State.html


object MyStackOne {

  type Stack = List[Int]

  def pop(stack: Stack): (Int, Stack) = {
    stack match {
      case x :: xs => (x, xs)
    }
  }

  def push(n: Int, stack: Stack): (Unit, Stack) = {
    ((), n :: stack)
  }

  def stackManipOne(stack: Stack): (Int, Stack) = {
    val (_, newStack1) = push(3, stack)
    val (n, newStack2) = pop(newStack1)
    pop(newStack2)
  }

}


// using State

object MyStackTwo {

  type Stack = List[Int]

  val pop = State[Stack, Int] {
    case x :: xs => (xs, x)
  }

  def push(a: Int) = State[Stack, Unit] {
    case xs => (a :: xs, ())
  }

  def stackManipTwo: State[Stack, Int] = {
    for {
      _ <- push(3)
      a <- pop
      b <- pop
    } yield b
  }

}


// pop and push in terms of get and put

object MyStackThree {

  type Stack = List[Int]

  val pop: State[Stack, Int] = {
    for {
      s <- get[Stack]
      (x :: xs) = s
      _ <- put(xs)
    } yield x
  }

  def push(x: Int): State[Stack, Unit] = {
    for {
      xs <- get[Stack]
      r <- put(x :: xs)
    } yield r
  }

  def stackManipThree: State[Stack, Int] = {
    for {
      _ <- push(3)
      a <- pop
      b <- pop
    } yield b
  }

}


object StackExample extends App {

  import MyStackOne._
  import MyStackTwo._

  val (n1, stack1) = stackManipOne(List(5, 8, 2, 1))
  println(s"Example 1 => elem: $n1, stack: $stack1")

  val (stack2, n2) = stackManipTwo(List(5, 8, 2, 1))
  println(s"Example 2 => elem: $n2, stack: $stack2")

  val (stack3, n3) = stackManipTwo(List(5, 8, 2, 1))
  println(s"Example 3 => elem: $n3, stack: $stack3")

}
