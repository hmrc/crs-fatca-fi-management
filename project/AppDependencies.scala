import sbt._

object AppDependencies {

  private val bootstrapVersion = "9.11.0"
  private val domainVersion = "10.0.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"  %% "bootstrap-backend-play-30" % bootstrapVersion,
    "uk.gov.hmrc"  %% "domain-play-30"            % domainVersion,
    "com.beachape" %% "enumeratum-play"           % "1.8.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.mockito"                %% "mockito-scala"          % "1.17.31",
    "wolfendale"                 %% "scalacheck-gen-regexp"  % "0.1.2",
    "org.jsoup"                   % "jsoup"                  % "1.17.2",
    "org.scalatest"              %% "scalatest"              % "3.2.18",
    "org.scalatestplus"          %% "scalacheck-1-17"        % "3.2.18.0",
    "com.softwaremill.quicklens" %% "quicklens"              % "1.9.7"
  ).map(_ % Test)

  val itDependencies: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30" % bootstrapVersion % Test
  )

}
