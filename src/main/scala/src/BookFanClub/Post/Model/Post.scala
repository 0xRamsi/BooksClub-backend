package src.BookFanClub.Post.Model

case class Post(
    id: String = java.util.UUID.randomUUID.toString,
    userName: String,
    bookId: String,
    header: String,
    body: String
)
