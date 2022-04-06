package bioinformatics

import scala.collection.mutable.ListBuffer
import scala.io.Source

case class Sequence(
                   location: Loci,
                   sequence: String
                   ) {

  def complement(): Sequence = {
    Sequence(
      location,
      sequence.toUpperCase()
        .replace("A", "t")
        .replace("T", "a")
        .replace("G", "c")
        .replace("C", "g")
        .toUpperCase()
    )
  }

  def reverse(): Sequence = {
    Sequence(
      location,
      sequence.reverse
    )
  }

  def generateLoci(): List[Loci] = {

    val buf = ListBuffer[Loci]()
    var i = location.startLocation

    while (i < location.endLocation) {
      var j = i + 1
      while (j <= location.endLocation) {
        buf += Loci(location.chromosome, location.strand, i, j)
        j += 1
      }
      i += 1
    }
    buf.toList
  }

}

object Sequence {
  def fromFastaFile(infileName: String): Sequence = {
    val lines = Source.fromFile(infileName).getLines().toList
    val header = lines.head
    val body = lines(1)
    Sequence(Loci.fromHeader(header), body)
  }
}


