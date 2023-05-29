package src.BookFanClub.Author.Repositories

import src.BookFanClub.Author.Model.Author

import scala.concurrent.Future

trait AuthorRepository {
  def allAuthors(
      limit: Int = 100,
      offset: Int = 0,
      title: Option[String] = None
  ): Future[Seq[Author]]

  def author(id: String): Future[Option[Author]]
  def authors(ids: Seq[String]): Future[Seq[Author]]
  def addAuthor(author: Author): Future[Author]
  def deleteAuthor(id: String): Future[Option[Author]]

}
