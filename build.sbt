name := """personal-hub"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.18"

libraryDependencies += guice
libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.13.1"
libraryDependencies += "org.jsoup" % "jsoup" % "1.22.1"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "5.6.3"

coverageExcludedPackages := Seq(
  "controllers.Reverse.*",
  "controllers.javascript.Reverse.*",
  "router.Routes.*"
).mkString(";")

