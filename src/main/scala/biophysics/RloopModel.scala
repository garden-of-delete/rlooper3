package biophysics

import scala.collection.JavaConverters._
import scala.math.{Pi,pow}

import com.typesafe.config.Config

import bioinformatics.{Loci, EnergizedLoci, SuperhelicalDomainMap, Sequence}

case class RloopModel(
    helicalPitch: Double,
    singleStrandTortionalStiffness: Double,
    tempInKelvin: Double,
    idealGasConstant: Double,
    nucleationFreeEnergy: Double,
    homopolymerOverride: Boolean,
    overrideEnergy: Double,
    bpEnergetics: DinucleotideEnergies
) extends EnergeticModel {

  override def computeGroundState(domainSize: Int, domainSuperhelicity: Double): Double = {
    //((k*pow(alpha, 2)) / 2) - a;
    val linkingDifference: Double = domainSize * domainSuperhelicity * helicalPitch
    val hookesLawCoefficient: Double = 2200 * idealGasConstant * tempInKelvin / domainSize
    hookesLawCoefficient*pow(linkingDifference, 2) / 2 - nucleationFreeEnergy
  }

  def computeGroundStateFactor(domainSize: Int, domainSuperhelicity: Double): Double = {
    computeBoltzmannFactor(computeGroundState(domainSize, domainSuperhelicity), tempInKelvin)
  }

  override def computeStructure(
      loci: EnergizedLoci,
      domainSize: Int,
      domainSuperhelicity: Double
  ): Structure = {

    val linkingDifference: Double = domainSize * domainSuperhelicity * helicalPitch
    val hookesLawCoefficient: Double = 2200 * idealGasConstant * tempInKelvin / domainSize

    val superHelicalEnergy: Double =
      (2 * pow(Pi, 2)
        * singleStrandTortionalStiffness
        * hookesLawCoefficient
        * pow(linkingDifference + loci.getLength * helicalPitch, 2)) /
        (4 * pow(Pi, 2)
        * singleStrandTortionalStiffness
        + hookesLawCoefficient * loci.getLength)


    val freeEnergy = superHelicalEnergy //+ loci.bpEnergy
    val boltzmannFactor = computeBoltzmannFactor(freeEnergy, tempInKelvin)

    Structure("rloop", loci, freeEnergy, boltzmannFactor)
  }

  def getDinucleotideEnergy(bps: String): Double = {
    bps match {
      case "CC" => bpEnergetics.rGG_dCC
      case "CG" => bpEnergetics.rGC_dCG
      case "CT" => bpEnergetics.rGA_dCT
      case "CA" => bpEnergetics.rGU_dCA
      case "GC" => bpEnergetics.rCG_dGC
      case "GG" => bpEnergetics.rCC_dGG
      case "GT" => bpEnergetics.rCA_dGT
      case "GA" => bpEnergetics.rCU_dGA
      case "TC" => bpEnergetics.rAG_dTC
      case "TG" => bpEnergetics.rAC_dTG
      case "TT" => bpEnergetics.rAA_dTT
      case "TA" => bpEnergetics.rAU_dTA
      case "AC" => bpEnergetics.rUG_dAC
      case "AG" => bpEnergetics.rUC_dAG
      case "AT" => bpEnergetics.rUA_dAT
      case "AA" => bpEnergetics.rUU_dAA
      case _ => println(s"Error: Unrecognized dinucleotides: ${bps}"); 0
    }
  }

  def computeBpEnergy(loci: Loci, inputSequence: Sequence): EnergizedLoci = {
    var energy: Double = 0
    var i: Int = 1
    while (i < inputSequence.sequence.length) {
      energy += getDinucleotideEnergy(inputSequence.sequence.slice(i-1,i+1))
      i += 1
    }

    EnergizedLoci.fromLoci(loci, energy)
  }

}

object RloopModel {

  def apply(conf: Config): RloopModel = {
    val modelConfigPath = "models.rloopEquilibriumModel"
    val bpEnergies = conf.getConfig(s"${modelConfigPath}.bpEnergetics")
      .entrySet()
      .asScala
      .map(e => e.getKey -> e.getValue.render())
      .toMap
      .mapValues(_.toDouble)
    new RloopModel(
      conf.getDouble(s"${modelConfigPath}.helicalPitch"),
      conf.getDouble(s"${modelConfigPath}.singleStrandTortionalStiffness"),
      conf.getDouble(s"${modelConfigPath}.tempInKelvin"),
      conf.getDouble(s"${modelConfigPath}.idealGasConstant"),
      conf.getDouble(s"${modelConfigPath}.nucleationFreeEnergy"),
      conf.getBoolean(s"${modelConfigPath}.homopolymerOverride"),
      conf.getDouble(s"${modelConfigPath}.overrideEnergy"),
      DinucleotideEnergies(
        bpEnergies("rGG_dCC"),
        bpEnergies("rGC_dCG"),
        bpEnergies("rGA_dCT"),
        bpEnergies("rGU_dCA"),
        bpEnergies("rCG_dGC"),
        bpEnergies("rCC_dGG"),
        bpEnergies("rCA_dGT"),
        bpEnergies("rCU_dGA"),
        bpEnergies("rAG_dTC"),
        bpEnergies("rAC_dTG"),
        bpEnergies("rAA_dTT"),
        bpEnergies("rAU_dTA"),
        bpEnergies("rUG_dAC"),
        bpEnergies("rUC_dAG"),
        bpEnergies("rUA_dAT"),
        bpEnergies("rUU_dAA")
      )
    )
  }

}
