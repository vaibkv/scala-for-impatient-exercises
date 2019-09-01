package Chap_17_Futures

import java.time.LocalDateTime
import java.util.concurrent.Executors

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * Using futures, run four tasks that each sleep for ten seconds and then print the current time. If
 * you have a reasonably modern computer, it is very likely that it reports four available
 * processors to the JVM, and the futures should all complete at around the same time. Now repeat
 * with forty tasks. What happens? Why? Replace the execution context with a cached thread pool.
 * What happens now? (Be careful to define the futures after replacing the implicit execution
 * context.)
 */

//Global thread pool -> only manages a small number of threads (by default, equal to the number of cores of all processors).
//create your own thread pool. Many options in Executors java class depending on your use case

object Eleven extends App {

  val coresORp = Runtime.getRuntime.availableProcessors()
  def f() = {
    Thread.sleep(10000)
    println(Thread.currentThread().getId)
    println(LocalDateTime.now())
  }

  //runWithGlobalPool(4, f)
  //Prints -
  //14
  //15
  //16
  //13
  //2019-08-31T19:50:51.848
  //2019-08-31T19:50:51.848
  //2019-08-31T19:50:51.848
  //2019-08-31T19:50:51.848

  //runWithGlobalPool(20, f)
  //Prints -
  /*23
  20
  19
  22
  15
  17
  18
  24
  14
  16
  13
  21
  2019-08-31T19:52:04.982
  2019-08-31T19:52:04.982
  2019-08-31T19:52:04.983
  2019-08-31T19:52:04.982
  2019-08-31T19:52:04.982
  2019-08-31T19:52:04.982
  2019-08-31T19:52:04.983
  2019-08-31T19:52:04.982
  2019-08-31T19:52:04.982
  2019-08-31T19:52:04.982
  2019-08-31T19:52:04.983
  2019-08-31T19:52:04.982
  15
  18
  22
  16
  21
  24
  14
  17
  2019-08-31T19:52:14.999
  2019-08-31T19:52:15
  2019-08-31T19:52:14.999
  2019-08-31T19:52:14.999
  2019-08-31T19:52:14.999
  2019-08-31T19:52:14.999
  2019-08-31T19:52:14.999
  2019-08-31T19:52:14.999*/

  runWithLocalPool(20, f)
  /*Prints - //Almost all at the same time!
  32
  20
  17
  13
  18
  14
  15
  26
  28
  31
  27
  30
  29
  16
  25
  24
  21
  22
  23
  19
  2019-08-31T19:54:53.603
  2019-08-31T19:54:53.601
  2019-08-31T19:54:53.602
  2019-08-31T19:54:53.601
  2019-08-31T19:54:53.601
  2019-08-31T19:54:53.601
  2019-08-31T19:54:53.603
  2019-08-31T19:54:53.601
  2019-08-31T19:54:53.602
  2019-08-31T19:54:53.601
  2019-08-31T19:54:53.602
  2019-08-31T19:54:53.603
  2019-08-31T19:54:53.603
  2019-08-31T19:54:53.601
  2019-08-31T19:54:53.603
  2019-08-31T19:54:53.603
  2019-08-31T19:54:53.603
  2019-08-31T19:54:53.601
  2019-08-31T19:54:53.601
  2019-08-31T19:54:53.601*/

  def runWithGlobalPool(parallel: Int, func: () => Unit): Unit = {
    import scala.concurrent.ExecutionContext.Implicits._

    val futuresSeq = (1 to parallel).map(_ => Future {func()})
    val resFuture = Future.sequence(futuresSeq)

    Await.result(resFuture, Duration.Inf)
  }

  def runWithLocalPool(parallel: Int, func: () => Unit): Unit = {
    val pool = Executors.newScheduledThreadPool(parallel)
    implicit val ec = ExecutionContext.fromExecutor(pool)

    val futuresSeq = (1 to parallel).map(_ => Future {func()})
    val resFuture = Future.sequence(futuresSeq)

    Await.result(resFuture, Duration.Inf)
  }
}