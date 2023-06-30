package src.BookFanClub

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.ActorContext
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import src.BookFanClub.Author.AuthorRoutes
import src.BookFanClub.Book.BookRoutes
import src.BookFanClub.DependencyInjection.Guice.MyInjector.injector
import src.BookFanClub.Post.PostRoutes
import src.BookFanClub.Post.Repository.PostRepository.PostRepositoryTrait
import src.BookFanClub.Server.Message

object RoutesGenerator {
  def generateRoutes(ctx: ActorContext[Message])(implicit system: ActorSystem[Nothing]): Route = {
    import net.codingwell.scalaguice.InjectorExtensions.*

    val index = path("") {
      get {
        getFromResource("index.html")
      }
    }

    val PostRepository = ctx.spawn(injector.instance[PostRepositoryTrait], "PostRepository")
    val postRoutes     = new PostRoutes(PostRepository)
    val cors           = new CORSHandler {}

    cors.corsHandler(
      concat(
        index,
        postRoutes.thePostRoutes,
        injector.instance[BookRoutes].bookRoutes,
        injector.instance[AuthorRoutes].authorRoutes
      )
    )
  }
}
