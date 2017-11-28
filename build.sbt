name := "facility-calculator"

version := "0.1"

organization := "com.foobar"

scalaVersion := "2.12.3"

// The necessary dependencies can be added here

val sparkVersion = "2.1.0"
libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.1",
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)
        