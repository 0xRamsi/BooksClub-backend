package src.BookFanClub.Books.model

case class Book(id: String = java.util.UUID.randomUUID.toString, name: String, authorId: String)
