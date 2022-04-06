name := "rlooper"

version := "0.1"

val scala212Version = "2.12.10"
val sparkVersion = "3.1.2"

val sparkArtifact = "org.apache.spark" %% "spark-sql" % sparkVersion //% "provided"
val typeSafe = "com.typesafe" % "config" % "1.4.2"
val scalaTestDep = "org.scalatest" %% "scalatest" % "3.2.+" % "test"
val scalaMockDep = "org.scalamock" %% "scalamock" % "5.2.0" % "test"


scalaVersion := scala212Version

assembly / assemblyJarName := s"${name.value}_${scalaBinaryVersion.value}-${sparkVersion}_${version.value}.jar"
assembly / test := {}
assembly / assemblyMergeStrategy := {
  case PathList("org", "apache", "spark", "unused", "UnusedStubClass.class")
    => MergeStrategy.first
  case PathList(ps @ _*) if ps.last == "module-info.class"
    => MergeStrategy.first
  case PathList("META-INF", "MANIFEST.MF")
    => MergeStrategy.discard
  case x => MergeStrategy.first
}

libraryDependencies ++= Seq(
  sparkArtifact,
  typeSafe,
  scalaTestDep,
  scalaMockDep
)
