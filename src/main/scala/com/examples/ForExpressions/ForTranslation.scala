package com.examples.ForExpressions


// examples of progfun2 course

// the Scala compiler translates for-expressions in terms of map, flatMap and a lazy variant of filter

object ForTranslation extends App {

  var list1 = List(1, 2, 3)
  var list2 = List(4, 5, 6)

  // map
  for (x <- list1) yield x * 2
  list1 map (x => x*2)
  // res: List[Int] = List(2, 4, 6)

  // filter
  for (x <- list1 if x % 2 == 0) yield x
  for (x <- list1 withFilter(_ % 2 == 0)) yield x
  // res: List[Int] = List[2]

  // flatMap
  for (x <- list1; y <- list2) yield x + y
  list1 flatMap(x => for(y <- list2) yield x + y)
  // res: List[Int] = List(5, 6, 7, 6, 7, 8, 7, 8, 9)


  // simplify combinations of core methods map, flatMap, filter:

  var n = 5
  def isPrime(n: Int): Boolean = ! ((2 until n-1) exists (n % _ == 0))

  // instead of:
  (1 until n) flatMap (i =>
    (1 until i) filter (j => isPrime(i + j)) map
      (j => (i, j)))

  // one can write:
  for {
    i <- 1 until n
    j <- 1 until i
    if isPrime(i + j)
  } yield (i, j)


  // foreach vs for-loops

  for (i <- 1 until 3; j <- "abc") println(i + "" + j)
  (1 until 3) foreach (i => "abc" foreach (j => println(i + "" + j)))
  // 1a1b1c2a2b2c

}
