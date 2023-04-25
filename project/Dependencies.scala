import sbt.*
object Dependencies {
  lazy val sttpVersion = "3.8.15"

  lazy val catsCore = "org.typelevel" %% "cats-core" % "2.9.0"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.15"
  lazy val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "2.1.0"
  lazy val sttpCore = "com.softwaremill.sttp.client3" %% "core" % sttpVersion
  lazy val sttpFs2 = "com.softwaremill.sttp.client3" %% "fs2" % sttpVersion
  lazy val sttpUpickle = "com.softwaremill.sttp.client3" %% "upickle" % sttpVersion
  lazy val sttpZio = "com.softwaremill.sttp.client3" %% "zio" % sttpVersion
  lazy val wireMock = "com.github.tomakehurst" % "wiremock-jre8" % "2.35.0"
  lazy val zioInteropCats = "dev.zio" %% "zio-interop-cats" % "23.0.0.0"
}
