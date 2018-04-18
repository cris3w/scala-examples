package com.examples.MonadState


// example of Timothy Perrett's blog:
// http://timperrett.com/2013/11/25/understanding-state-monad/


sealed trait Aspect
case object Green extends Aspect
case object Amber extends Aspect
case object Red   extends Aspect


sealed trait Mode
case object Off      extends Mode
case object Flashing extends Mode
case object Solid    extends Mode


case class Signal(
  isOperational: Boolean,
  display: Map[Aspect, Mode])


object Signal {

  import scalaz.State
  import scalaz.State._


  type ->[A, B] = (A, B)
  type SignalState[A] = State[Signal, A]


  val default = Signal(
    isOperational = false,
    display = Map(Red -> Flashing, Amber -> Off, Green -> Off))


  def enable: State[Signal, Boolean] =
    for {
      a <- init // unnecessary
      _ <- modify((s: Signal) => s.copy(isOperational = true))
      r <- get
    } yield {
      println(s"enable>a: $a") // debugging
      println(s"enable>r: $r") // debugging
      r.isOperational
    }


  def halt  = change(Red -> Solid, Amber -> Off,   Green -> Off)
  def ready = change(Red -> Solid, Amber -> Solid, Green -> Off)
  def go    = change(Red -> Off,   Amber -> Off,   Green -> Solid)
  def slow  = change(Red -> Off,   Amber -> Solid, Green -> Off)


  private def change(seq: Aspect -> Mode*): State[Signal, Map[Aspect, Mode]] =
    for {
      m <- init // unnecessary
      _ <- modify(display(seq))
      signal <- get
    } yield {
      println(s"change>m: $m") // debugging
      println(s"change>r: $signal") // debugging
      signal.display
    }


  private def display(seq: Seq[Aspect -> Mode]): Signal => Signal = signal =>
    if (signal.isOperational)
      signal.copy(display = signal.display ++ seq.toMap)
    else default

}


object TrafficLightExample extends App {

  import Signal._

  import scalaz.State.{get => current}

  val program = for {
    _  <- enable
    r0 <- current
    _  <- halt
    r1 <- current
    _  <- ready
    r2 <- current
    _  <- go
    r3 <- current
    _  <- slow
    r4 <- current
  } yield r0 :: r1 :: r2 :: r3 :: r4 :: Nil

  program.eval(default).zipWithIndex.foreach { case (v, i) => println(s"r$i - $v") }

}
