/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.perftests.example

import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.example.AlcoholDutyRequests._

class AlcoholDutySimulation extends PerformanceTestRunner {
  setup("auth-login-page", "Auth login page") withRequests getAuthLoginPage

  setup("login-authloginstub-page", "Login authloginstub page") withRequests loginWithAuthLoginStub

  setup("product-name-page", "Product name page") withRequests navigateToProductNamePage

  setup("post-product-name", "Post product name") withRequests postProductName

  setup("get-alcohol-by-volume-question-page", "Get alcohol by volume question page") withRequests getAlcoholbyVolumeQuestion

  runSimulation()
}
