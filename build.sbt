name := "scala-sbt11-template"

version := "1.0"

scalaVersion := "2.9.1"

sbtVersion := "0.11.0"

sbtPlugin := true

scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8")


libraryDependencies ++= Seq(
	"junit" % "junit" % "4.8.1" % "test",
	"org.hamcrest" % "hamcrest-library" % "1.1" % "test",
	"org.mockito" % "mockito-core" % "1.8.5" % "test",
	"org.specs2" %% "specs2" % "1.6.1",
	"org.scalatest" %% "scalatest" % "1.6.1" % "test",		
	"org.slf4j" % "slf4j-api" % "1.6.1",
	"org.slf4j" % "jcl-over-slf4j" % "1.6.1",
	"ch.qos.logback" % "logback-classic" % "0.9.28",
	"org.clapper" %% "grizzled-slf4j" % "0.6.6"
)
