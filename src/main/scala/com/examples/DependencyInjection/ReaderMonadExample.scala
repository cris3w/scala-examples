package com.examples.DependencyInjection

import scalaz.Reader


// example based on a post of Scalera Blog:
// https://scalerablog.wordpress.com/2015/10/21/teoria-de-cate-movidas-monada-reader/


case class Book(isbn: String, name: String)


trait Repository {
  def get(isbn: String): Book
  def getAll: List[Book]
}

class RepositoryImp extends Repository {

  override def get(isbn: String): Book =
    Book(isbn, s"name_of_$isbn")

  override def getAll: List[Book] =
    List(Book("1", s"name_of_1"), Book("2", s"name_of_2"))
}


trait Library {
  import scalaz.Reader

  def getBook(isbn: String): Reader[Repository, Book] =
    Reader((repository: Repository) => repository.get(isbn))

  def getAllBooks: Reader[Repository, List[Book]] =
    Reader((repository: Repository) => repository.getAll)
}

object LibraryInfo extends Library {

  def bookName(isbn: String) =
    getBook(isbn) map (_.name)
}


class UniversityApp(repository: Repository) extends Library {

  // GET ~/books/{id}/name/
  def getBookName(isbn: String) =
    run(LibraryInfo.bookName(isbn))

  // GET ~/books/
  def getAll = run(getAllBooks)

  private def run[A](reader: Reader[Repository, A]): A = {
    reader(repository)
  }
}

object UniversityApp extends UniversityApp(new RepositoryImp)


object ReaderMonadExample extends App {
  import UniversityApp._

  for {
    book <- getAll
  } yield println(book)

}
