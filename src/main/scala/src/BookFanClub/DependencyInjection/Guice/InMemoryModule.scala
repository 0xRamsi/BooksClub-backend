package src.BookFanClub.DependencyInjection.Guice

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.{ScalaModule, ScalaPrivateModule}
import src.BookFanClub.Author.Repositories.{AuthorRepository, InMemoryAuthorRepository}
import src.BookFanClub.Books.Repositories.{BookRepository, InMemoryBookRepository}

class InMemoryModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[BookRepository].to[InMemoryBookRepository]
    bind[AuthorRepository].to[InMemoryAuthorRepository]
  }
}
