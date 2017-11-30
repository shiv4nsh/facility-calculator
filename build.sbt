name := "facility-calculator"

version := "0.1"

organization := "com.foobar"

scalaVersion := "2.11.11"

coverageEnabled := true

// The necessary dependencies can be added here

val sparkVersion = "2.1.0"
libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.1",
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "com.github.vagmcs" %% "optimus" % "2.1.0",
  "com.github.vagmcs" %% "optimus-solver-oj" % "2.1.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)
        