package Chap_17_Futures

import scala.annotation.tailrec
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.StdIn

/** Write a program that counts the prime numbers between 1 and n, as reported by
BigInt.isProbablePrime. Divide the interval into p parts, where p is the number of
available processors. Count the primes in each part in concurrent futures and combine the
results. */

object Seven extends App {
  import scala.concurrent.ExecutionContext.Implicits._

  val coresORp = Runtime.getRuntime.availableProcessors()
  println ("Enter the value of n: ")
  val n = StdIn.readInt()

  if (coresORp > n) {
    throw new IllegalArgumentException(s"$n should be less than or equal to $coresORp")
  }

  val x = countAllPrimes(n, coresORp)

  def countAllPrimes(n: Int, p: Int): Int = {
    val size = (n / p).ceil.toInt
    val buckets = (0 until p).map(partition => {  //until means to p-1
      val start = 1 + (size * partition)
      val end = if (partition == p-1) n else start + (size - 1)
      (start to end).toSeq
    })

    //traverse takes in a collection, applies a future function to each element (element -> Future[B]) and returns a Future[collection[B]]
    val seqF = Future.traverse(buckets)(bucket => Future {
      bucket.reduceLeft((x, acc) => if(BigInt(x).isProbablePrime(1)) 1 + acc else 0 + acc)
    })

    Await.result(seqF.map(_.sum), Duration.Inf) //basically doing seqofElements.sum since seqF.map gives result of this future, which is a seq. note that f.map also returns a map
  }
}