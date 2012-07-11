package com.github.sett4.descgpx

/**
 * Created with IntelliJ IDEA.
 * User: sett4
 * Date: 12/05/31
 * Time: 0:31
 * To change this template use File | Settings | File Templates.
 */

import scala.io.Source
import scala.xml._
import scala.xml.pull._
import java.text.{SimpleDateFormat}
import java.util
import collection.mutable
import org.geotools.referencing.GeodeticCalculator

object App {
  def main(args: Array[String]): Unit = {
    println("hogehoge")
    val url = getClass.getResourceAsStream("/commuting.gpx")
    val source = Source.fromInputStream(url)
    val reader = new GpxReader(new XMLEventReader(source))
    reader.read()

    val calc = new GeodeticCalculator(org.geotools.referencing.crs.DefaultGeographicCRS.WGS84)
    //reader.trackpoints.foreach( (x) => println("new Y.LatLng(%s,%s),".format(x.lat, x.lon)))
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    reader.trackpoints.sliding(2).foreach( (pair) => {
      calc.setStartingGeographicPoint(pair(0).lon, pair(0).lat)
      calc.setDestinationGeographicPoint(pair(1).lon, pair(1).lat)

      val ele = pair(1).ele.getOrElse(0.0) - pair(0).ele.getOrElse(0.0);
      val distanceInMeters = math.sqrt(math.pow(calc.getOrthodromicDistance, 2) + math.pow(ele, 2))
      val timeInHours = math.abs(pair(0).time.getTime - pair(1).time.getTime).toDouble / 1000 / 60 / 60
      val kmph = if (timeInHours != 0.0) distanceInMeters / timeInHours / 1000 else 0.0
      println("%s\t%f\t%f".format(dateFormat.format(pair(1).time), distanceInMeters, kmph))
    })
  }
}

case class TrackPoint(time: util.Date, lat: Double, lon: Double, ele: Option[Double], speed: Option[Double], cad: Option[Double])

class GpxReader(val reader: XMLEventReader) extends HalfEventParser(reader) {
  val trackpoints = mutable.ListBuffer[TrackPoint]()

  def read() = {
    super.read({
      case e @ EvElemStart(_, "metadata", _, _) => onMetadata
      case e @ EvElemStart(_, "trkpt", _, _) => onTrkpt
    });
  }

  def onMetadata(elem: Elem) = {
//    println(elem.toString())
  }

  val utcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  utcDateFormat.setTimeZone(util.TimeZone.getTimeZone("UTC"))

  def onTrkpt(elem: Elem): Unit = {
    val time: util.Date = utcDateFormat.parse((elem \\ "time").text)
    val lon = elem.attribute("lon").getOrElse("0.0").toString.toDouble
    val lat = elem.attribute("lat").getOrElse("0.0").toString.toDouble
    val ele = if ((elem \\ "ele").length>0) Some((elem \\ "ele").text.toDouble) else None
    val cad = if ((elem \\ "cad").length>0) Some((elem \\ "cad").text.toDouble) else None
    val speed = if ((elem \\"speed").length>0) Some((elem \\"speed").text.toDouble) else None
    val t = TrackPoint(time, lat, lon, ele, speed, cad)
    trackpoints += t
  }
}