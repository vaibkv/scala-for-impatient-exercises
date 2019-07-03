package Chap_17_Futures

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/*Write a function doInOrder that, given two functions f: T => Future[U] and g: U
=> Future[V], produces a function T => Future[V] that, for a given t, eventually
yields g(f(t))*/

object Two extends App {

  import ExecutionContext.Implicits._

  def f(t: Int) = Future { (t + 2).toString}
  def g(u: String) = Future { u.concat(" is now done") }
  val x = doInOrder(f, g)

  val res = Await.result(x.apply(2), Duration.Inf)
  println(res)

  private def doInOrder[T, U, V](f: T => Future[U], g: U => Future[V]): T => Future[V] = {
    (t: T) => f(t).flatMap(u => g(u)) //read the chapter on futures and see why we can't use .map! Map will give us future inside a future, whereas we want chaining
  }

}