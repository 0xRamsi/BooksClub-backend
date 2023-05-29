package src.BookFanClub.DependencyInjection.Guice

import com.google.inject.{Guice, Injector}
import src.BookFanClub.DependencyInjection.Guice.InMemoryModule

object MyInjector {
  val injector: Injector = Guice.createInjector(InMemoryModule())
}
