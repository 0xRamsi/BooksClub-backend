package src.BookFanClub.Formatters.Json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.PathMatcher.Lift.MOps.OptionMOps
import spray.json.DefaultJsonProtocol.*
import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import src.BookFanClub.Author.Model.Author
import src.BookFanClub.Books.model.Book

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val bookFormat: RootJsonFormat[Book]     = jsonFormat3(Book.apply)
  implicit val authorFormat: RootJsonFormat[Author] = jsonFormat2(Author.apply)
}
