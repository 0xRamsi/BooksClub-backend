package src.BookFanClub.Book

import akka.http.scaladsl.server.Directives.*
import com.google.inject.Inject
import spray.json.*
import src.BookFanClub.Book.Repositories.BookRepository
import src.BookFanClub.Formatters.Json.JsonSupport

class BookRoutes @Inject() (bookRepository: BookRepository) extends JsonSupport {
  lazy val bookRoutes =
    concat(
      path("books") {
        get {
          onSuccess(bookRepository.allBooks()) { books =>
            complete(books.toJson)
          }
        }
      },
      pathPrefix("book" / Segment) { id =>
        get {
          onSuccess(bookRepository.book(id = id)) { bookData =>
            complete(bookData.toJson)
          }
        }
      }
    )
}
