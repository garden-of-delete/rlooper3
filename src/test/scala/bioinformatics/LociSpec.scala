package bioinformatics


import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.io.Source

class LociSpec extends AnyWordSpec with Matchers {
  "fromHeader" when {
    "given a valid header" should {
      val header = Source.fromFile("src/test/resources/pfc53_full.fa").getLines().toList.head
      "construct a matching Loci" in {
        val loci = Loci.fromHeader(header)
        loci.chromosome should equal (">HG19_AIRN_PFC53_REVERSE_dna")
        loci.strand should equal ("-")
        loci.startLocation should equal (1)
        loci.endLocation should equal (3908)
      }
    }
  }
}
