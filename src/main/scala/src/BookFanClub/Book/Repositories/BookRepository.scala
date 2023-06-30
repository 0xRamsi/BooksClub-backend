package src.BookFanClub.Book.Repositories

import src.BookFanClub.Book.model.Book

import scala.concurrent.Future

trait BookRepository {
  def allBooks(
      limit: Int = 100,
      offset: Int = 0,
      title: Option[String] = None
  ): Future[Seq[Book]]

  def book(id: String): Future[Option[Book]]
  def books(id: Seq[String]): Future[Seq[Book]]
  def booksByAuthor(id: String): Future[Seq[Book]]
  def addBook(book: Book): Future[Book]
  def deleteBook(id: String): Future[Option[Book]]
}
