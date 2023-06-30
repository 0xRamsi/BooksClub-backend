package src.BookFanClub.Post.Repository

import akka.actor.typed.{Behavior, Signal, TypedActorContext}
import akka.actor.typed.scaladsl.Behaviors
import com.google.inject.Singleton
import src.BookFanClub.Post.Model.Post
import src.BookFanClub.Post.Repository.PostRepository.*

@Singleton
class InMemoryPostRepository extends PostRepositoryTrait {
  override def receive(ctx: TypedActorContext[Command], msg: Command): Behavior[Command] =
    InMemoryPostRepository.apply()

  override def receiveSignal(ctx: TypedActorContext[Command], msg: Signal): Behavior[Command] =
    InMemoryPostRepository.apply()
}

object InMemoryPostRepository {
  def apply(posts: Map[String, Seq[Post]] = Map.empty): Behavior[Command] = Behaviors.receiveMessage {
    case GetAllPosts(replyTo) => // For testing.
      val res = posts.values.flatten.toSeq
      replyTo ! res
      Behaviors.same
    case GetPostsForBook(bookId, replyTo) =>
      replyTo ! posts.getOrElse(bookId, Seq.empty)
      Behaviors.same
    case AddPost(post, replyTo) =>
      replyTo ! OK
      val existingPostsForBook = posts.getOrElse(post.bookId, Seq())
      InMemoryPostRepository(posts - post.bookId + (post.bookId -> (existingPostsForBook :+ post)))
    case DeletePost(post, replyTo) =>
      val newBookPosts = posts.getOrElse(post.bookId, Seq.empty).filter(_ != post)
      val newPosts =
        if (newBookPosts.isEmpty)
          posts - post.bookId
        else
          posts - post.bookId + (post.bookId -> newBookPosts)
      InMemoryPostRepository(newPosts)
  }
}
