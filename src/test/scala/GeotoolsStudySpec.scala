import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.geotools.referencing.{GeodeticCalculator, CRS}

/**
 * Created with IntelliJ IDEA.
 * User: sett4
 * Date: 12/07/12
 * Time: 0:11
 * To change this template use File | Settings | File Templates.
 */

class GeotoolsStudySpec extends FunSpec with ShouldMatchers {
  describe("referencing study") {
    it("can convert") {
      val calc = new GeodeticCalculator(org.geotools.referencing.crs.DefaultGeographicCRS.WGS84)
      calc.setStartingGeographicPoint(139.163192734122276, 35.439350120723248)
      calc.setDestinationGeographicPoint(139.163207989186049, 35.439376858994365)
      calc.getOrthodromicDistance should equal(3.274008179593667);
    }
  }
}