/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.perftests.example

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.example.AlcoholDutyRequests._

class AlcoholDutySimulation extends PerformanceTestRunner {

  setup("product-name-page", "Product name page") withRequests navigateToProductNamePage

  setup("post-product-name", "Post product name") withRequests postProductName

  setup("get-alcohol-by-volume-question-page", "Get alcohol by volume question page") withRequests getAlcoholbyVolumeQuestion

  runSimulation()
}
