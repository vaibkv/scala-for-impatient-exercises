package Chap_17_Futures

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.io.{Source, StdIn}

/**
 * Write a program that asks the user for a URL, reads the web page at that URL, and displays all
 * the hyperlinks. Use a separate Future for each of these three steps.
 */

object Eight extends App {
  import scala.concurrent.ExecutionContext.Implicits._

  val askUserF = Future { StdIn.readLine("Give me some url: ") }//"https://docs.scala-lang.org/tutorials/scala-for-java-programmers.html"
  def readPageF(url: String) = Future {
    val html = Source.fromURL(url)("ISO-8859-1") //encoding was important here!
    html.mkString
  }
  def displayHyperLinksF(content: String) = Future {
    //some lame logic to get hrefs
    val contentSeq = content.split("\n")
    contentSeq.foreach(s => {
      if (s.contains("href") || s.contains("HREF")) {
        println(s)
      }
    })
  }

  val result = askUserF.flatMap(
    url => readPageF(url).
      flatMap(content => displayHyperLinksF(content)))
  Await.result(result, Duration.Inf)
}