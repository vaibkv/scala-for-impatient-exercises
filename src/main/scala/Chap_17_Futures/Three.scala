package Chap_17_Futures

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

//Repeat the preceding exercise for any sequence of functions of type T => Future[T].

//What I understand -> In earlier exercise we had chaining of two functions, now we need to chain an entire sequence of functions -> result of one goes into second,
//result of second goes into third and so on...till we get a final function where you can simply enter a value and the whole sequence gets executed and you get a final value.
//I think for simplicity, author has just kept T as generic type

object Three extends App {

  import ExecutionContext.Implicits._

  val f1 =  (t: Int) => Future {println("I got called first"); t + 2}
  val f2 = (t: Int) => Future {println("I got called second?"); t + 5}
  val f3 = (t: Int) => Future {println(s"I am f3 and I will print result from f2: $t"); t + 10}
  val seqT = Seq(f1, f2, f3) //Note - I HAD to change above def's to val's so that I can create this sequence!

  val x = doInOrder(seqT)

  val result = Await.result(x.apply(3), Duration.Inf)

  println(result.toString)

  private def doInOrder[T](seqT: Seq[T => Future[T]]): T => Future[T] = {

    seqT.reduceLeft((acc, fn) => {
      t: T => acc(t).flatMap(u => fn(u))
    })

  }

}