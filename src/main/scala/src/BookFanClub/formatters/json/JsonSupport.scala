package src.BookFanClub.Formatters.Json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.PathMatcher.Lift.MOps.OptionMOps
import akka.util.Collections
import spray.json.*
import spray.json.DefaultJsonProtocol.*
import src.BookFanClub.Author.Model.Author
import src.BookFanClub.Book.model.Book
import src.BookFanClub.Post.Model.Post
import src.BookFanClub.Post.Repository.PostRepository

import scala.collection.immutable.{AbstractSeq, LinearSeq}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val bookFormat: RootJsonFormat[Book]     = jsonFormat3(Book.apply)
  implicit val authorFormat: RootJsonFormat[Author] = jsonFormat2(Author.apply)
  implicit val postFormat: RootJsonFormat[Post]     = jsonFormat5(Post.apply)
  implicit val postResultPostResponseFormat: RootJsonFormat[PostRepository.PostResponse] = jsonFormat1(
    PostRepository.PostResponse.apply
  )
  implicit val postResultMaybePostResponseFormat: RootJsonFormat[PostRepository.MaybePostResponse] = jsonFormat1(
    PostRepository.MaybePostResponse.apply
  )
  implicit val postResultPostSeqResponseFormat: RootJsonFormat[PostRepository.PostSeqResponse] = jsonFormat1(
    PostRepository.PostSeqResponse.apply
  )

  implicit object PostRepositoryResponseFormat extends RootJsonFormat[PostRepository.Response] {
    override def write(response: PostRepository.Response): JsValue = response match
      case post: PostRepository.PostResponse =>
        JsObject("type" -> JsString("PostResponse"), "content" -> postResultPostResponseFormat.write(post))
      case post: PostRepository.MaybePostResponse =>
        JsObject("type" -> JsString("MaybePostResponse"), "content" -> postResultMaybePostResponseFormat.write(post))
      case posts: PostRepository.PostSeqResponse =>
        JsObject("type" -> JsString("PostSeqResponse"), "content" -> postResultPostSeqResponseFormat.write(posts))
      case PostRepository.OK =>
        JsObject("type" -> JsString("OK"), "content" -> JsNull)

    override def read(json: JsValue): PostRepository.Response = json.asJsObject.getFields("type", "content") match {
      case Seq(JsString("PostResponse"), content)      => postResultPostResponseFormat.read(content)
      case Seq(JsString("MaybePostResponse"), content) => postResultMaybePostResponseFormat.read(content)
      case Seq(JsString("PostSeqResponse"), content)   => postResultPostSeqResponseFormat.read(content)
      case Seq(JsString("OK"), JsNull)                 => PostRepository.OK
      case _                                           => deserializationError("PostRepository.Response expected")
    }
  }
}
