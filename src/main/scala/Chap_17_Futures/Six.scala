package Chap_17_Futures

import scala.concurrent.Future

/** Write a method -
  * Future[T] repeat(action: => T, until: T => Boolean)
  * that asynchronously repeats the action until it produces a value that is accepted by the until
  * predicate, which should also run asynchronously. Test with a function that reads a password
  * from the console, and a function that simulates a validity check by sleeping for a second and
  * then checking that the password is "secret". Hint: Use recursion.*/

//Note - For the layman, whenever the book says "composing functions", it simply means "chaining functions". Science
// of any kind simply loves terminology and students should know how to simplify it!

//Most helpful on what a map actually does is Scala documentation itself -
/** Creates a new future by applying a function to the successful result of
 *  this future. If this future is completed with an exception then the new
 *  future will also contain this exception.
 *
 *  Example:
 *
 *  {{{
 *  val f = Future { "The future" }
 *  val g = f map { x: String => x + " is now!" }
 *  }}}
 */

//Actual difference between map and flatMap for Futures!
//1. someFuture.map(x => someFunction(x)) or something similar means that someFunction will run only after someFuture
// successfully returns a value x or it errors out and gives exception. The result of a map is a future.
//2. Now, if someFunction above were let's say to be independent of x and takes long time, then we would to let it run
// independently and then combine the result, we need to wrap up someFunction in a future and can do something like ->
// someFuture.map(_ Future {someFunction}). However, this would result Future[Future[T]] kind of thing. To avoid this, we can use flatMap,
// which is basically map and then it flattens it out. someFuture.flatMap(_ Future {someFunction}) returns Future[] only.
//3. A good explanation is also here - https://stackoverflow.com/questions/31641190/futures-map-vs-flatmap/31641623
/*Imagine the following functions :

def getFileNameFromDB{id: Int) : Future[String] = ???
def downloadFile(fileName: String) : Future[java.io.File] = ???
def processFile(file: java.io.File) : Future[ProcessResult] = ???
You could use flatMap to combine them :

val futResult: Future[ProcessResult] =
getFileNameFromDB(1).flatMap( name =>
downloadFile(name).flatMap( file =>
processFile(file)
)
)
Or using a for comprehension :

val futResult: Future[ProcessResult] =
for {
name <- getFileNameFromDB(1)
file <- downloadFile(name)
result <- processFile(file)
} yield result*/




/*If you have a future, let's say, Future[HttpResponse], and you want to specify what to do with that result when it is ready, such as write the body to a file, you may do something like responseF.map(response => write(response.body). However if write is also an asynchronous method which returns a future, this map call will return a type like Future[Future[Result]].

In the following code:

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

val numF = Future{ 3 }

val stringF = numF.map(n => Future(n.toString))

val flatStringF = numF.flatMap(n => Future(n.toString))
stringF is of type Future[Future[String]] while flatStringF is of type Future[String]. Most would agree, the second is more useful. Flat Map is therefore useful for composing multiple futures together.

When you use for comprehensions with Futures, under the hood flatMap is being used together with map.*/

//4. Note that flatMap always expects futures in its input, while when we use map, we can have input as future or simply any function


object Six extends App {

  private def nonRecursiverepeat[T](action:() => T, until: T => Boolean): Future[T] = {
    while(true) {

    }
  }

  private def recursiverepeat[T](action:() => T, until: T => Boolean): Future[T] = {
    null
  }

}