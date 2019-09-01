package Chap_17_Futures

/**
 * Write a program that asks the user for a URL, reads the web page at that URL, finds all the
 * hyperlinks, visits each of them concurrently, and locates the Server HTTP header for each of
 * them. Finally, print a table of which servers were found how often. The futures that visit each
 * page should return the header.
 */

object Nine extends App {
  import scala.concurrent.ExecutionContext.Implicits._
  //Not creating a crawler today. Maybe later!
}