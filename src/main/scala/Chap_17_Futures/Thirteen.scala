package Chap_17_Futures

/**
 * Use a promise for implementing cancellation. Given a range of big integers, split the range into
 * subranges that you concurrently search for palindromic primes. When such a prime is found, set
 * it as the value of the future. All tasks should periodically check whether the promise is
 * completed, in which case they should terminate.
 */

//Well, the task is not very clear for a Promises noob. I'd rather study Promises first (see 'Notes' section below)

object Thirteen extends App {
  import scala.concurrent.ExecutionContext.Implicits._
}

// Notes - A Future object is read-only. The value of the future is set implicitly when the task has finished or
// failed. A Promise is similar, but the value can be set explicitly.

// Three good resources to study about Scala promises are -
// 1. Programming in Scala, 3rd Edition
// 2. https://docs.scala-lang.org/overviews/core/futures.html (see in Downloaded Docs)
// 3. https://danielwestheide.com/blog/2013/01/16/the-neophytes-guide-to-scala-part-9-promises-and-futures-in-practice.html (see in Downloaded Docs)

// Let's first understand WHY we might sometimes need Promises -
// Consider the following example from scala docs -
/*
val p = Promise[T]()
val f = p.future

val producer = Future {
  val r = produceSomething()
  p success r
  continueDoingSomethingUnrelated()
}

val consumer = Future {
  startDoingSomething()
  f foreach { r =>
    doSomethingWithResult()
  }
}
 */
/*
In the above, there are two independent futures - producer and consumer. They are probably running off on different threads.
Now, if something is needed in a method (consumer here) that's produced by a differed async method (producer here), using
Promises is a good - we know producer would run before consumer (that's how producer-consumer problems are!) and so the value
of future will be all set for the consumer to consume.
 */