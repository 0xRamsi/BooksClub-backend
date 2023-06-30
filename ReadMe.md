## Backend of an example project.

This is an example project, which I used to learn Scala. It was inspired by Tasks that I got for coding interviews.
It demonstrated basic concepts, of DI and uses [Akka][1] as an HTTP-Server.

## How to run it

Note that this will only run the backend part.

```
git clone https://github.com/0xRamsi/BooksClub-backend.git
cd backend
sbt run
```

---

See also the Frontend part, for this project. You can of course just run it and use curl. Here are some commands:

> curl localhost:8081/books

> curl localhost:8081/authors

> curl localhost:8081/book/{one of the IDs from the first request} 

---

* **Why Akka and not the play-framework?**  
 Play would have been a more sensible choice for a commercial application. This is a learning project, and
my goal is to learn Scala, and demonstrate some concepts that I learned. 

[1]: https://akka.io/