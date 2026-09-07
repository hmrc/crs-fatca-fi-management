import sbt._

object AppDependencies {

  private val bootstrapVersion = "10.8.0"
  private val domainVersion = "13.0.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"  %% "bootstrap-backend-play-30" % bootstrapVersion,
    "uk.gov.hmrc"  %% "domain-play-30"            % domainVersion,
    "com.beachape" %% "enumeratum-play"           % "1.9.8"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.mockito"                %% "mockito-scala"          % "2.2.3",
    "io.github.wolfendale"       %% "scalacheck-gen-regexp"   % "1.1.0",
    "org.jsoup"                   % "jsoup"                  % "1.23.2",
    "org.scalatest"              %% "scalatest"              % "3.2.20",
    "org.scalatestplus"          %% "scalacheck-1-17"        % "3.2.18.0",
    "com.softwaremill.quicklens" %% "quicklens"              % "1.9.15"
  ).map(_ % Test)

  val itDependencies: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30" % bootstrapVersion % Test
  )

}
