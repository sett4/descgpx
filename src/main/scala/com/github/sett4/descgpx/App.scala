package com.github.sett4.descgpx

/**
 * Created with IntelliJ IDEA.
 * User: sett4
 * Date: 12/05/31
 * Time: 0:31
 * To change this template use File | Settings | File Templates.
 */

import scala.io.Source
import scala.concurrent.SyncVar
import scala.xml._
import scala.xml.parsing._
import scala.xml.pull._

object App {
  def main(args: Array[String]): Unit = {
    println("hogehoge")
    val url = getClass.getResourceAsStream("/commuting.gpx")
    val source = Source.fromInputStream(url)
    val reader = new GpxReader(new XMLEventReader(source))
    reader.read()
  }
}

class GpxReader(val reader: XMLEventReader) extends HalfEventParser(reader) {
  def acceptPf = {
    case e @ EvElemStart(_, "metadata", _, _) => onMetadata
    case e @ EvElemStart(_, "trkpt", _, _) => onTrkpt
  }

  def onMetadata(elem: Elem) = {
    println(elem.toString())
  }

  def onTrkpt(elem: Elem) = {
    println(elem.toString())
  }
}