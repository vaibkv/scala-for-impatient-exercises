package Chap_17_Futures

import scala.concurrent.{ExecutionContext, Future}

/*
Write a function that receives a sequence of futures and returns a future that eventually yields a
sequence of all results.
 */

object Five extends App {
  import ExecutionContext.Implicits._

  private def doAllTogether[T](seqF: Seq[Future[T]]): Future[Seq[T]] = {
    Future.sequence(seqF)
  }
}