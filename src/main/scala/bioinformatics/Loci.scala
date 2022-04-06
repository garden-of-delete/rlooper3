package bioinformatics

import scala.math.abs

case class Loci(
    chromosome: String,
    strand: String,
    startLocation: Int,
    endLocation: Int
) {

  def getLength: Int = {
    abs(startLocation - endLocation)
  }

  def key: String = {
    s"$chromosome|$strand|$startLocation|$endLocation"
  }
}

object Loci {
  def fromHeader(header: String): Loci = {
    val splitHeader = header.split(' ')
    val chromosome = splitHeader(0)
    val strand = splitHeader(4).split("=")(1)
    val start_location = splitHeader(1).split(":")(1).split("-")(0).toInt
    val end_location = splitHeader(1).split(":")(1).split("-")(1).toInt
    Loci(chromosome, strand, start_location, end_location)
  }

}
