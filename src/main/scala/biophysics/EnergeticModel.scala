package biophysics

import scala.math.exp
import bioinformatics.{EnergizedLoci, SuperhelicalDomainMap}

trait EnergeticModel {

  def computeBoltzmannFactor(energy: Double, temp: Double): Double = {
    exp(-1 * energy / 0.0019858775 * temp)
  }

  def computeGroundState(domainSize: Int, domainSuperhelicity: Double): Double

  def computeStructure(
      loci: EnergizedLoci,
      domainSize: Int,
      domainSuperhelicity: Double
  ): Structure
}
