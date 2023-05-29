package src.BookFanClub.Author.Repositories

import com.google.inject.Singleton
import src.BookFanClub.Author.Model.Author

import scala.concurrent.Future

@Singleton
class InMemoryAuthorRepository extends AuthorRepository {
  private var theAuthors: List[Author] = List(
    Author(id = "Rowling", name = "J. K. Rowling"),
    Author(id = "Marting", name = "Georg R. R. Martin"),
    Author(id = "Dalai Lama", name = "Dalai Lama")
  )

  override def allAuthors(limit: Int, offset: Int, title: Option[String]): Future[Seq[Author]] =
    Future.successful(theAuthors)

  override def author(id: String): Future[Option[Author]] =
    Future.successful(theAuthors.find(author => author.id == id))

  override def authors(ids: Seq[String]): Future[Seq[Author]] =
    Future.successful(theAuthors.filter(author => ids.contains(author.id)))

  override def addAuthor(author: Author): Future[Author] = {
    theAuthors = author :: theAuthors
    Future.successful(author)
  }

  override def deleteAuthor(id: String): Future[Option[Author]] = {
    val deleted = theAuthors.find(author => author.id == id)
    theAuthors = theAuthors.filter(_ != deleted)
    Future.successful(deleted)
  }
}
