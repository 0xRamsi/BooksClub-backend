package src.BookFanClub

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.HttpHeader.*
import akka.http.scaladsl.model.HttpMethods.*
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.{Directives, Route}
import com.google.inject.Guice
import spray.json.*
import src.BookFanClub.Author.Model.Author
import src.BookFanClub.Author.Repositories.AuthorRepository
import src.BookFanClub.Books.Repositories.BookRepository
import src.BookFanClub.DependencyInjection.Guice.MyInjector.injector
import src.BookFanClub.Formatters.Json.JsonSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object SetupServer extends Directives with JsonSupport {
  def entryPoint(): Unit = {
    import net.codingwell.scalaguice.InjectorExtensions.*
    val booksRepo  = injector.instance[BookRepository]
    val authorRepo = injector.instance[AuthorRepository]

    val index = path("") {
      get {
        getFromResource("index.html")
      }
    }
    val books = path("books") {
      get {
        onSuccess(booksRepo.allBooks()) { books =>
          complete(books.toJson)
        }
      }
    }
    val authors = path("authors") {
      get {
        onSuccess(authorRepo.allAuthors()) { authors =>
          complete(authors.toJson)
        }
      }
    }
    val book = pathPrefix("book" / Segment) { id =>
      get {
        onSuccess(booksRepo.book(id = id)) { bookData =>
          complete(bookData.toJson)
        }
      }
    }
    val author = pathPrefix("author" / Segment) { id =>
      get {
        onSuccess(authorRepo.author(id = id)) { authorData =>
          complete(authorData.toJson)
        }
      }
    }
    val routes: Route = {
      respondWithHeader(RawHeader("Access-Control-Allow-Origin", "*")) {
        index ~ books ~ authors ~ book ~ author
      }
    }

    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val bindingFuture: Future[Http.ServerBinding] = Http().newServerAt("localhost", 8081).bind(routes)

    println("Server started")
    println("Press Enter to stop the server, and exit the program.")
    StdIn.readLine() // let it run until user presses return

    bindingFuture
      .flatMap(_.unbind())                 // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}

@main def main(): Unit = SetupServer.entryPoint()
