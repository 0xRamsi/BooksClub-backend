package src.BookFanClub.Books.Repositories

import com.google.inject.{Inject, Singleton}
import src.BookFanClub.Books.Repositories.BookRepository
import src.BookFanClub.Books.model.Book

import scala.concurrent.Future

@Singleton
class InMemoryBookRepository extends BookRepository {

  private var theBooks: List[Book] = List(
    Book(id = java.util.UUID.randomUUID.toString, name = "Harry Potter", authorId = "Rowling"),
    Book(id = java.util.UUID.randomUUID.toString, name = "Game of Thrones", authorId = "Marting"),
    Book(id = java.util.UUID.randomUUID.toString, name = "The Universe in a single Atom", authorId = "Dalai Lama")
  )
  override def allBooks(limit: Int, offset: Int, title: Option[String]): Future[Seq[Book]] = Future.successful(theBooks)
  override def book(id: String): Future[Option[Book]] = Future.successful(theBooks.find(book => book.id == id))
  override def books(ids: Seq[String]): Future[Seq[Book]] =
    Future.successful(theBooks.filter(book => ids.contains(book.id)))
  override def booksByAuthor(authorId: String): Future[Seq[Book]] =
    Future.successful(theBooks.filter(book => book.authorId == authorId))
  override def addBook(book: Book): Future[Book] = {
    theBooks = book :: theBooks
    Future.successful(book)
  }
  override def deleteBook(id: String): Future[Option[Book]] = {
    val deletedBook: Option[Book] = theBooks.find(book => book.id == id)
    theBooks = theBooks.filter(book => book.id != id)
    Future.successful(deletedBook)
  }
}
