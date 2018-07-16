package com.examples.DependencyInjection

import cats.data._
import cats.effect.{IO, Sync}


case class Book3(isbn: String, name: String)


trait Repository3[F[_]] {

  def get(isbn: String): F[Book3]

  def getAll: F[List[Book3]]
}


class RepositoryImp3[F[_]](implicit val F: Sync[F]) extends Repository3[F] {

  override def get(isbn: String): F[Book3] =
    F.pure(Book3(isbn, s"name_of_$isbn"))

  override def getAll: F[List[Book3]] =
    F.pure(List(Book3("1", s"name_of_1"), Book3("2", s"name_of_2")))
}


trait Library3[F[_]] {

  def getBook(isbn: String): Kleisli[F, Repository3[F], Book3] =
    Kleisli((repository: Repository3[F]) => repository.get(isbn))

  def getAllBooks: Kleisli[F, Repository3[F], List[Book3]] =
    Kleisli((repository: Repository3[F]) => repository.getAll)
}


class LibraryInfo3[F[_]](implicit val F: Sync[F]) extends Library3[F] {

  def bookName(isbn: String): Kleisli[F, Repository3[F], String] =
    getBook(isbn) map (_.name)
}


class UniversityApp3[F[_]](repository: Repository3[F], library: LibraryInfo3[F])(implicit val F: Sync[F]) {

  // GET ~/books/{id}/name/
  def getBookName(isbn: String): F[String] =
    run(library.bookName(isbn))

  // GET ~/books/
  def getAll: F[List[Book3]] =
    run(library.getAllBooks)

  private def run[A](reader: Kleisli[F, Repository3[F], A]): F[A] = {
    reader(repository)
  }
}


object UniversityApp3 extends UniversityApp3[IO](new RepositoryImp3[IO], new LibraryInfo3[IO])


object ReaderMonadWithIOCatsExample extends App {
  import UniversityApp3._

  val pureProgram = for {
    books <- getAll
    _ <- IO(println(books))
  } yield books

  pureProgram.unsafeRunSync()
}
