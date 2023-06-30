package src.BookFanClub.Post.Repository

import akka.actor.typed.{ActorRef, Behavior, ExtensibleBehavior}
import src.BookFanClub.Post.Model.Post

object PostRepository {
  trait PostRepositoryTrait extends ExtensibleBehavior[Command]
  sealed trait Response
  final case class PostResponse(post: Post)              extends Response
  final case class MaybePostResponse(post: Option[Post]) extends Response
  final case class PostSeqResponse(posts: Seq[Post])     extends Response
  case object OK                                         extends Response

  sealed trait Command
  final case class GetAllPosts(replyTo: ActorRef[Seq[Post]])                     extends Command
  final case class GetPostsForBook(bookId: String, replyTo: ActorRef[Seq[Post]]) extends Command
  final case class AddPost(post: Post, replyTo: ActorRef[Response])              extends Command
  final case class DeletePost(post: Post, replyTo: ActorRef[Response])           extends Command
}
