package src.BookFanClub.Author

import akka.http.scaladsl.server.Directives.*
import com.google.inject.Inject
import spray.json.*
import src.BookFanClub.Author.Repositories.AuthorRepository
import src.BookFanClub.Formatters.Json.JsonSupport

class AuthorRoutes @Inject() (authorRepository: AuthorRepository) extends JsonSupport {
  lazy val authorRoutes =
    concat(
      path("authors") {
        get {
          onSuccess(authorRepository.allAuthors()) { authors =>
            complete(authors.toJson)
          }
        }
      },
      pathPrefix("author" / Segment) { id =>
        get {
          onSuccess(authorRepository.author(id = id)) { authorData =>
            complete(authorData.toJson)
          }
        }
      }
    )
}
