package bioinformatics

import scala.collection.mutable.ListBuffer

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SequenceSpec extends AnyWordSpec with Matchers {

  "fromFastaFile" when {
    "given a valid filename" should {
      val filename = "src/test/resources/pfc53_full.fa"
      "construct a matching Sequence" in {
        val sequence = Sequence.fromFastaFile(filename)
        sequence.sequence.take(5) should equal ("CCAGT")
      }
    }
  }

  "complement" when {
    "given a valid sequence" should {
      val testLoci = Loci("A", "+", 1, 7)
      val testSequence = Sequence(testLoci, "GATTACA")
      "return the complement Sequence" in {
        val complementSequence = testSequence.complement()
        complementSequence.sequence should equal ("CTAATGT")
      }
    }
  }
  "invert" when {
    "given a valid sequence" should {
      val testLoci = Loci("A", "+", 1, 7)
      val testSequence = Sequence(testLoci, "GATTACA")
      "return the complement Sequence" in {
        val invertedSequence = testSequence.reverse()
        invertedSequence.sequence should equal ("ACATTAG")
      }
    }
  }
  "generateLoci" when {
    "given a valid sequence" should {
      val testLoci = Loci("A", "+", 1, 7)
      val testSequence = Sequence(testLoci, "GATTACA")
      "produce the correct List[Loci]" in {
        val t = testSequence.generateLoci()
        println(t)
      }
    }
  }
}
