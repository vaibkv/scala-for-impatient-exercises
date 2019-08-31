/*Consider the expression
for (n1 <- Future { Thread.sleep(1000) ; 2 }
  n2 <- Future { Thread.sleep(1000); 40 })
println(n1 + n2)
How is the expression translated to map and flatMap calls? Are the two futures executed
  concurrently or one after the other? In which thread does the call to println occur?*/

package Chap_17_Futures

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object One extends App {
  import scala.concurrent.ExecutionContext.Implicits._

  //verion1()

  letsPrintSomething()

  def letsPrintSomething(): Unit = {
    val x = Future {Thread.sleep(1000) ;2}
    val y = Future {Thread.sleep(1000) ;40}

    val z = x.flatMap(v1 => y.map(v2 => v2 + v1)) //this is basically equal to what we have in for in version1()

    z.onComplete {
      case Success(n3) => println(n3)
      case Failure(ex) => throw ex
    }

    Await.result(z, Duration.Inf) //basically blocking the main thread from completing till we get z done!, otherwise nothing will get printed
  }

  def verion1(): Unit = {
    val startTime = System.currentTimeMillis()

    val z = for {
      x <- Future {Thread.sleep(10000) ;2}
      y <- Future {Thread.sleep(10000) ;40}
    } yield x + y

    val timeElapsed = System.currentTimeMillis() - startTime

    println(z.value) //mostly prints None!. Why? println is part of 'main' thread and fires futures and comes back to println immediately!
    //If you decompile Scala to Java using intelliJ, you will see the above being translated to .map and .flatMap. x+y is translated to flatMap

    println(timeElapsed) // Since this is almost always coming out to be 120ms or so, so the futures are always executed concurrently, not one after another
  }
}