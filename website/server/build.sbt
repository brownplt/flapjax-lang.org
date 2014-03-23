name := "flapjax-server"

version := "2.0"

organization := "edu.umass.cs"

autoAPIMappings := true

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-feature"

scalaVersion := "2.10.3"

//
// Akka
//
 
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.0"
 
//
// Web server
//

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies += "io.spray" % "spray-routing" % "1.3.1"

libraryDependencies += "io.spray" % "spray-can" % "1.3.1"

libraryDependencies += "io.spray" % "spray-caching" % "1.3.1"

//
// Logging
//

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.9"

libraryDependencies += "ch.qos.logback" % "logback-core" % "1.0.9"

lazy val logback = "ch.qos.logback" % "logback-classic" % "1.0.9"
