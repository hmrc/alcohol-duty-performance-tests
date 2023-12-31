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

import io.gatling.core.action.builder.ActionBuilder
import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.example.AlcoholDutyRequests._
import uk.gov.hmrc.perftests.example.DeclareDutySuspendedDeliveriesRequests._

class AlcoholDutySimulation extends PerformanceTestRunner {

  val AlcoholDutyReturnsJourney: List[ActionBuilder] =
    List[ActionBuilder](
      getAuthLoginPage,
      loginWithAuthLoginStub,
      navigateToProductNamePage,
      postProductName,
      getAlcoholByVolumeQuestion,
      postAlcoholByVolume,
      getDraughtReliefQuestion,
      postDraughtReliefQuestion,
      getSmallProducerReliefQuestion,
      postSmallProducerReliefQuestion
    )

  setup("alcohol-duty-returns-journey", "Alcohol Duty Returns Journey") withActions
    (AlcoholDutyReturnsJourney: _*)

  val DeclareDutySuspendedDeliveriesJourney: List[ActionBuilder] =
    List[ActionBuilder](
      getAuthLoginPage,
      loginWithAuthLoginStub,
      navigateToProductNamePage,
      postProductName,
      getDeclareDutySuspendedDeliveriesQuestion,
      postDeclareDutySuspendedDeliveriesQuestion,
      getDutySuspendedDeliveriesGuidance,
      getDeclareDutySuspendedDeliveriesOutsideUk,
      postDeclareDutySuspendedDeliveriesOutsideUk,
      getDeclareDutySuspendedDeliveriesInsideUk,
      postDeclareDutySuspendedDeliveriesInsideUk,
      getDeclareDutySuspendedReceived,
      postDeclareDutySuspendedReceived,
      getCheckYourAnswersDutySuspendedDeliveries
      //postCheckYourAnswersDutySuspendedDeliveries
    )
  setup("declare-duty-suspended-deliveries-journey", "Declare Duty Suspended Deliveries Journey") withActions
    (DeclareDutySuspendedDeliveriesJourney: _*)

  runSimulation()
}
