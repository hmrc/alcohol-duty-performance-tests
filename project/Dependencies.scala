import sbt.{ModuleID, * }

object Dependencies {

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "performance-test-runner" % "6.1.0"
  )
}