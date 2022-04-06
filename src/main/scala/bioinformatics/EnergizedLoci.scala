package bioinformatics

import scala.math.abs

case class EnergizedLoci(
    chromosome: String,
    strand: String,
    startLocation: Int,
    endLocation: Int,
    bpEnergy: Double
) {

  def getLength: Int = {
    abs(startLocation - endLocation)
  }
}

object EnergizedLoci {
  def fromLoci(loci: Loci, bpEnergy: Double): EnergizedLoci = {
    EnergizedLoci(
      loci.chromosome,
      loci.strand,
      loci.startLocation,
      loci.endLocation,
      bpEnergy
    )
  }
}
