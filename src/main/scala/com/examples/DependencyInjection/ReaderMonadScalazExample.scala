package com.examples.DependencyInjection

import scalaz.Reader


// example based on a post of Scalera Blog:
// https://scalerablog.wordpress.com/2015/10/21/teoria-de-cate-movidas-monada-reader/


case class Book1(isbn: String, name: String)


trait Repository1 {

  def get(isbn: String): Book1

  def getAll: List[Book1]
}


class RepositoryImp1 extends Repository1 {

  override def get(isbn: String): Book1 =
    Book1(isbn, s"name_of_$isbn")

  override def getAll: List[Book1] =
    List(Book1("1", s"name_of_1"), Book1("2", s"name_of_2"))
}


trait Library1 {

  def getBook(isbn: String): Reader[Repository1, Book1] =
    Reader((repository: Repository1) => repository.get(isbn))

  def getAllBooks: Reader[Repository1, List[Book1]] =
    Reader((repository: Repository1) => repository.getAll)
}


object LibraryInfo1 extends Library1 {

  def bookName(isbn: String) =
    getBook(isbn) map (_.name)
}


class UniversityApp1(repository: Repository1) extends Library1 {

  // GET ~/books/{id}/name/
  def getBookName(isbn: String) =
    run(LibraryInfo1.bookName(isbn))

  // GET ~/books/
  def getAll = run(getAllBooks)

  private def run[A](reader: Reader[Repository1, A]): A = {
    reader(repository)
  }
}


object UniversityApp1 extends UniversityApp1(new RepositoryImp1)


object ReaderMonadExample extends App {
  import UniversityApp1._

  for {
    book <- getAll
  } yield println(book)
}
