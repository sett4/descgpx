import com.github.sett4.descgpx.{GpxReader, App}
import io.Source
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{FunSpec}
import xml.pull.XMLEventReader

/**
 * Created with IntelliJ IDEA.
 * User: sett4
 * Date: 12/05/31
 * Time: 1:10
 * To change this template use File | Settings | File Templates.
 */


class AppSpec extends FunSpec with ShouldMatchers {

  describe("app") {
    it("run app") {
      App.main(Array[String]())
    }
  }

  describe("iterator study test") {
    it("takeWhileで切り出したイテレータを使うと元のイテレータも進む") {
      val it = Iterator("a", "number", "of", "words")
      val sub = it.takeWhile( _ != "number")
      sub.hasNext should equal(true)
      sub.next() should equal("a")
      it.hasNext should equal(true)
      it.next() should equal("number")
    }

    it("takeWhileで切り出したイテレータを使わないと元のイテレータ進まない") {
      val it = Iterator("a", "number", "of", "words")
      val sub = it.takeWhile( _ != "number")
      it.hasNext should equal(true)
      it.next() should equal("a")
    }

  }
}

