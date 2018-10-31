name := "some-fp-scala"

version := "0.1"

scalaVersion := "2.12.7"


addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4")

scalacOptions += "-Ypartial-unification"


libraryDependencies ++= Seq(

  "org.typelevel" %% "cats-core" % "1.4.0",
  "org.typelevel" %% "cats-mtl-core" % "0.4.0",
  "org.typelevel" %% "cats-effect" % "1.1.0-M1",

  "org.scalaz" %% "scalaz-core" % "7.3.0-M25",

  "com.github.julien-truffaut" %% "monocle-core" % "1.5.1-cats",

  "io.monix" %% "monix" % "2.3.3"
)
