import com.github.sett4.descgpx.HalfEventParser
import io.Source
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import xml.{Comment, Text, Elem}
import xml.pull.{EvElemStart, XMLEventReader}

/**
 * Created with IntelliJ IDEA.
 * User: sett4
 * Date: 12/06/20
 * Time: 0:14
 * To change this template use File | Settings | File Templates.
 */

class HalfEventParserSpec extends FunSpec with ShouldMatchers {

  describe("parse xml") {
    val source = Source.fromString("<a></a>")
    val reader = new XMLEventReader(source)
    val parser = new HalfEventParser(reader)

    val event = reader.next()
    val element = parser.toElem(event.asInstanceOf[EvElemStart])

    it("parse simple element") {
      element.label should equal("a")
    }
  }

  describe("parse xml depth 2") {
    val source = Source.fromString("""<a k="1"><b xmlns:book="http://example.com/ns/book/">aa<!-- comment --><h:title/></b><c/></a>""")
    val reader = new XMLEventReader(source)
    val parser = new HalfEventParser(reader)

    val event = reader.next()
    val element = parser.toElem(event.asInstanceOf[EvElemStart])

    it("parse simple element") {
      element.label should equal("a")
      element.child(0).label should equal("b")
      element.child(1).label should equal("c")
    }

    it("parse attribute") {
      element.attribute("k").get.text should equal("1")
    }

    it("parse text") {
      element.child(0).text should equal("aa")
    }

    it("parse namespace") {
      element.child(0).scope.prefix should equal("book")
      element.child(0).scope.uri should equal("http://example.com/ns/book/")
    }

    it("parse comment") {
      element.child(0).child(1).label should equal("#REM")
      element.child(0).child(1).asInstanceOf[Comment].commentText should equal(" comment ")
    }

    it("parse namespace prefix") {
      element.child(0).child(2).label should equal("title")
      element.child(0).child(2).prefix should equal("h")
    }
  }
}

