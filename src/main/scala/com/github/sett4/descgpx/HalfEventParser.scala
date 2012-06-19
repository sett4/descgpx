package com.github.sett4.descgpx

import xml._
import io.Source
import pull.EvComment
import pull.EvElemEnd
import pull.EvElemStart
import pull.EvEntityRef
import pull.EvProcInstr
import pull.EvText
import pull.XMLEvent
import pull.XMLEventReader
import xml.Text
import xml.Text
import xml.EntityRef
import xml.Comment
import scala.Some
import xml.ProcInstr

/**
 * Created with IntelliJ IDEA.
 * User: sett4
 * Date: 12/06/20
 * Time: 0:13
 * To change this template use File | Settings | File Templates.
 */

abstract class HalfEventParser(reader: XMLEventReader) {
  def this(source: Source) = {
    this(new XMLEventReader(source))
  }

  /**
   *
   * @param current
   * @param reader
   * @return
   */
  def toElem(orig: EvElemStart) : Elem = {
    var children = Seq[Node]()

    while (reader.hasNext) {
      val event = reader.next()

      event match {
        case EvElemEnd(_,_) =>
          return Elem(orig.pre, orig.label, orig.attrs, orig.scope, children: _*)
        case start: EvElemStart =>
          children = children ++ toElem(start)
        case text: EvText =>
          children = children ++ Text(text.text)
        case comment: EvComment =>
          children = children ++ Comment(comment.text)
        case entityRef: EvEntityRef =>
          children = children ++ EntityRef(entityRef.entity)
        case procInstr: EvProcInstr =>
          children = children ++ ProcInstr(procInstr.target, procInstr.text)
        case _ =>
      }
    }

    throw new IllegalStateException(orig.label+"の閉じタグが見つかりませんでした")
  }

  def read(): Unit = {
    while (reader.hasNext) {
      val event = reader.next()

      if (acceptPf.isDefinedAt(event) == true) {
        val handler = acceptPf(event)
        if (event.isInstanceOf[EvElemStart] == false) {
          throw new IllegalStateException("event is not instance of EvElemStart")
        }
        handler(toElem(event.asInstanceOf[EvElemStart]))
      }
    }
  }

  def skipPf: PartialFunction[XMLEvent, (Elem => Unit)] = {
    case _ => { (x) => }
  }

  def acceptPf: PartialFunction[XMLEvent, (Elem => Unit)]
}
