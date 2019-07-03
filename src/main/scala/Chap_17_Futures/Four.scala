package Chap_17_Futures

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/*
Write a function doTogether that, given two functions f: T => Future[U] and g: U
=> Future[V], produces a function T => Future[(U, V)], running the two
computations in parallel and, for a given t, eventually yielding (f(t), g(t)).

Note - There's definitely a typo. For us to have g(t), g should accept the same type as f. So, it should be g: T => Future[V]
 */

object Four extends App {
  import ExecutionContext.Implicits._

  def f(t: Int) = Future {t + 10}
  def g(t: Int) = Future {t / 5}

  val x = doTogether(f, g)
  val result = Await.result(x(10), Duration.Inf)

  println(result._1)
  println(result._2)

  private def doTogether[T, U, V](f: T => Future[U], g: T => Future[V]): T => Future[(U, V)] = {
    t: T => {
      val x = f(t)
      val y = g(t)
      x.flatMap(s => y.map(p => (s, p)))
    }
  }
}