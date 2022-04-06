package pipeline

//import org.apache.spark.sql.SparkSession
import com.typesafe.config.{Config, ConfigFactory}
import bioinformatics.Sequence
import biophysics.{RloopModel, Structure}
import org.apache.spark.{SparkConf, SparkContext}



object EnsembleAnalysisSingleRegion extends App {

  /*
  val spark = SparkSession.builder
    .master("local[*]")
    //.master("spark://127.0.0.1:59424")
    .appName("rlooper3")
    .getOrCreate()
   */

  val sparkConf = new SparkConf().setAppName("rlooper3").setMaster("local[*]")

  val sc = new SparkContext(sparkConf)
  val conf: Config = ConfigFactory.load()

  val rloopModel = sc.broadcast(RloopModel(conf))

  val inputSequence = Sequence.fromFastaFile("src/main/resources/pfc53_full.fa")
  if (inputSequence.location.strand == "-") {inputSequence.reverse()}
  else if(inputSequence.location.strand == "+") {inputSequence.complement()}

  val domainSize = inputSequence.sequence.length
  val domainSuperhelicity = -0.07

  //generate loci
  val ensembleLoci = sc.parallelize(inputSequence.generateLoci())
  //generate bp energies
  val bpEnergies = ensembleLoci.map(rloopModel.value.computeBpEnergy(_, inputSequence)) //TODO: Optimize

  val structures = bpEnergies.map(
    rloopModel.value.computeStructure(_, domainSize, domainSuperhelicity)
  )

  val partitionFunction = sc.broadcast(
    structures.map(_.boltzmannFactor).reduce(_ + _) +
      rloopModel.value.computeGroundStateFactor(domainSize, domainSuperhelicity)
  )

  println(partitionFunction.value)

  val structureProbs = structures.map(_.boltzmannFactor / partitionFunction.value)
  //val mostProbableRloop: Structure = structureProbs.
  //println(s"P(R-loop): ${structureProbs.reduce(_+_)}")
  println(s"Most probably R-loop found @ ")

  // bp_energies = inputSequence.sequence
  // map loci + bp energies to structures (computes free energy of each structure)
  // sum the free energies of all structures + ground state producing the partition function
  // broadcast value of partition function
  // Stage 3: map structures -> probabilities for each structure
  // collect and write results to wig / bedfiles

}
