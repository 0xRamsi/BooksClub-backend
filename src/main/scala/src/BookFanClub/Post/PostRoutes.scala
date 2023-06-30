package src.BookFanClub.Post

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.HttpMethods.contentLengthAllowedCommon
import akka.http.scaladsl.model.RequestEntityAcceptance.Expected
import akka.http.scaladsl.model.headers.{Allow, RawHeader}
import akka.http.scaladsl.model.{
  ContentType,
  HttpEntity,
  HttpHeader,
  HttpMethod,
  HttpMethods,
  HttpResponse,
  MediaTypes,
  StatusCode,
  StatusCodes
}
import akka.http.scaladsl.server.ContentNegotiator.Alternative.MediaType
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import spray.json.*
import src.BookFanClub.Formatters.Json.JsonSupport
import src.BookFanClub.Post.Model.Post
import src.BookFanClub.Post.Repository.InMemoryPostRepository
import src.BookFanClub.Post.Repository.PostRepository

import scala.collection.immutable
import scala.concurrent.Future
import scala.concurrent.duration.*
import scala.util.Try

class PostRoutes(buildPostRepository: ActorRef[PostRepository.Command])(implicit system: ActorSystem[_])
    extends JsonSupport {

  import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}

  implicit val timeout: Timeout = 3.seconds

  lazy val thePostRoutes: Route =
    concat(
      get {
        concat(
          path("post" / Segment) { id =>
            val getAllPosts: Future[Seq[Post]] =
              buildPostRepository.ask(PostRepository.GetPostsForBook(id, _))
            Thread.sleep(1000) // TODO: Hack because of race condition.
            onSuccess(getAllPosts) { bookPosts =>
              println("complete " + bookPosts)
              complete(bookPosts.toJson)
            }
          },
          path("posts") {
            val getAllPosts: Future[Seq[Post]] =
              buildPostRepository.ask(PostRepository.GetAllPosts.apply)
            Thread.sleep(1000)
            onSuccess(getAllPosts) { allPosts =>
              val l = allPosts :+ Post("id", "uerName", "???", "header", "body")
              complete(l.toJson)
            }
          }
        )
      },
      post {
        path("newPost") {
          entity(as[src.BookFanClub.Post.Model.Post]) { post =>
            if (post.header.isEmpty) {
              complete(StatusCodes.Forbidden -> "Not allowed!")
            } else {
              val operationPreformed: Future[PostRepository.Response] =
                buildPostRepository.ask(PostRepository.AddPost(post, _))
              Thread.sleep(1000)
              onSuccess(operationPreformed) { response =>
                complete(response.toJson)
              }
            }
          }
        }
      }
    )
}
