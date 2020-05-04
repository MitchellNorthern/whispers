name := "whispers"

version := "1.0"

scalaVersion := "2.13.1"

lazy val commonSettings = Seq(
    version := "1.0.0-SNAPSHOT"
)

lazy val app = (project in file(".")).
    settings(commonSettings: _*).
    settings(
        name := "whispers"
    ).
    enablePlugins(AssemblyPlugin)

resolvers ++= Seq(
    "Sbt plugins"                   at "https://dl.bintray.com/sbt/sbt-plugin-releases",
    "Maven Central Server"          at "http://repo1.maven.org/maven2"
)

mainClass in assembly := Some("Main")
