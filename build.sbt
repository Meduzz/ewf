name := "ewf"

version := "20180204"

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

organization := "se.chimps.ewf"

credentials += Credentials(Path.userHome / ".ivy2" / ".ewf")

publishTo := Some("se.chimps.ewf" at "https://yamr.kodiak.se/maven")

resolvers += "se.chimps.ewf" at "https://yamr.kodiak.se/maven"

publishArtifact in (Compile, packageDoc) := false

publishArtifact in (Compile, packageSrc) := false

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

libraryDependencies += "se.chimps.ewf" %% "ewf-api" % "20180204"