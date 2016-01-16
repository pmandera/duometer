import com.typesafe.sbt.SbtNativePackager._
import NativePackagerKeys._

name := "duometer"

organization := "com.pawelmandera"

version := "0.1.3"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.3.0",
  "org.apache.tika" % "tika-parsers" % "1.8" exclude("commons-logging", "commons-logging"),
  "org.specs2" %% "specs2" % "2.4.9" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

resolvers += Resolver.sonatypeRepo("public")

packageArchetype.java_application

packageDescription in Debian := "Near-duplicate detection tool"

maintainer in Debian := "Paweł Mandera <pawel.mandera@ugent.be>"
