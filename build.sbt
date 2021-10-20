name := "OTUS"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1",
  "com.github.scopt" %% "scopt" % "4.0.1"
)

idePackagePrefix := Some("ru.ognivenko")

Compile / mainClass := Some("ru.ogsbtnivenko.Search")
assembly / mainClass := Some("ru.ognivenko.Search")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
