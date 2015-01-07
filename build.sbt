import com.typesafe.sbt.SbtNativePackager._
import NativePackagerKeys._

name := "duometer"

organization := "com.pawelmandera"

version := "0.1.0"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.3.0",
  "org.specs2" %% "specs2" % "2.4.9" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers += Resolver.sonatypeRepo("public")

packageArchetype.java_application
