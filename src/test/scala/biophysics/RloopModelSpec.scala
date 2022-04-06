package biophysics

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import com.typesafe.config.ConfigFactory

class RloopModelSpec extends AnyWordSpec with Matchers {
  "apply" when {
    "given a valid Config" should {
      val model = RloopModel(ConfigFactory.load())
      "construct a matching RloopModel" in {
        model.helicalPitch should equal(0.09615384615384615)
        model.singleStrandTortionalStiffness should equal(1.8)
        model.tempInKelvin should equal(310)
        model.idealGasConstant should equal(0.0019858775)
        model.nucleationFreeEnergy should equal(10)
        model.homopolymerOverride should be(false)
        model.overrideEnergy should equal(0.0)
        model.getDinucleotideEnergy("CC") should equal(-0.36)
        model.getDinucleotideEnergy("AT") should equal(0.28)
      }
    }
  }
}
