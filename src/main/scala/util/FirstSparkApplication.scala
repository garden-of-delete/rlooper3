package util

import org.apache.spark.rdd.RDD

import scala.collection.immutable.Seq
import scala.util.Random
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

case class testThing(
                    name: String,
                    value: Int
                    )


  object FirstSparkApplication extends App {

    /*
    val spark = SparkSession.builder
      .master("local[*]")
      //.master("spark://127.0.0.1:59424")
      .appName("rlooper")
      .getOrCreate()

     */

    val conf = new SparkConf().setAppName("Spark Scala WordCount Example").setMaster("local[1]")
    val sc = new SparkContext(conf)

    def generateRandomTestThings(n: Int): Seq[testThing] = {
      val random = new Random
      Seq.fill(n)(testThing("test", Random.nextInt))
    }

    val collection: Seq[testThing] = generateRandomTestThings(1000000)

    val data: RDD[testThing] = sc.parallelize(collection)

    val sum = data.flatMap(_.name).sum()

    println(sum)

}
