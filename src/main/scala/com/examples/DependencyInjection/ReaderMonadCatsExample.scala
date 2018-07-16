package com.examples.DependencyInjection

import cats.data.Reader


case class Book2(isbn: String, name: String)


trait Repository2 {

  def get(isbn: String): Book2

  def getAll: List[Book2]
}


class RepositoryImp2 extends Repository2 {

  override def get(isbn: String): Book2 =
    Book2(isbn, s"name_of_$isbn")

  override def getAll: List[Book2] =
    List(Book2("1", s"name_of_1"), Book2("2", s"name_of_2"))
}


trait Library2 {

  def getBook(isbn: String): Reader[Repository2, Book2] =
    Reader((repository: Repository2) => repository.get(isbn))

  def getAllBooks: Reader[Repository2, List[Book2]] =
    Reader((repository: Repository2) => repository.getAll)
}


object LibraryInfo2 extends Library2 {

  def bookName(isbn: String): Reader[Repository2, String] =
    getBook(isbn) map (_.name)
}


class UniversityApp2(repository: Repository2) extends Library2 {

  // GET ~/books/{id}/name/
  def getBookName(isbn: String): String =
    run(LibraryInfo2.bookName(isbn))

  // GET ~/books/
  def getAll: List[Book2] =
    run(getAllBooks)

  private def run[A](reader: Reader[Repository2, A]): A = {
    reader(repository)
  }
}


object UniversityApp2 extends UniversityApp2(new RepositoryImp2)


object ReaderMonadCatsExample extends App {
  import UniversityApp2._

  for {
    book <- getAll
  } yield println(book)
}
