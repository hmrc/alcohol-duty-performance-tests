/*
 * Copyright 2024 HM Revenue & Customs
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
import uk.gov.hmrc.perftests.example.AlcoholDutyReturnsRequests._
import uk.gov.hmrc.perftests.example.DeclareDutySuspendedDeliveriesRequests._
import uk.gov.hmrc.perftests.example.DeclareQuarterlySpiritsQuestionsRequests._

class AlcoholDutySimulation extends PerformanceTestRunner {

  val AlcoholDutyReturnsJourneyWithSPRYes: List[ActionBuilder] =
    List[ActionBuilder](
      getAuthLoginPage,
      loginWithAuthLoginStub,
      getDeclareAlcoholDutyQuestion,
      postDeclareAlcoholDutyQuestion,
      getProductEntryGuidancePage,
      navigateToProductNamePage,
      postProductName,
      getAlcoholByVolumeQuestion,
      postAlcoholByVolume,
      getDraughtReliefQuestion,
      postDraughtReliefQuestion(true),
      getSmallProducerReliefQuestion,
      postSmallProducerReliefQuestion(true),
      getTaxTypeCode,
      postTaxTypeCode("Wine, tax type code 378"),
      getDeclareSmallProducerReliefDutyRate,
      postDeclareSmallProducerReliefDutyRate,
      getProductVolumePage,
      postProductVolumePage
    )

  setup(
    "alcohol-duty-returns-with-SPR-Yes-journey",
    "Alcohol Duty Returns Journey When Draught Relief and Small Producer Relief Selected Yes"
  ) withActions
    (AlcoholDutyReturnsJourneyWithSPRYes: _*)

  val AlcoholDutyReturnsJourneyWithSPRNo: List[ActionBuilder] =
    List[ActionBuilder](
      getAuthLoginPage,
      loginWithAuthLoginStub,
      getDeclareAlcoholDutyQuestion,
      postDeclareAlcoholDutyQuestion,
      getProductEntryGuidancePage,
      navigateToProductNamePage,
      postProductName,
      getAlcoholByVolumeQuestion,
      postAlcoholByVolume,
      getDraughtReliefQuestion,
      postDraughtReliefQuestion(false),
      getSmallProducerReliefQuestion,
      postSmallProducerReliefQuestion(false),
      getTaxTypeCode,
      postTaxTypeCode("Beer, tax type code 321", false),
      getProductVolumePage,
      postProductVolumePage
    )
  setup(
    "alcohol-duty-returns-with-SPR-No-journey",
    "Alcohol Duty Returns Journey When Draught Relief and Small Producer Relief Selected No"
  ) withActions
    (AlcoholDutyReturnsJourneyWithSPRNo: _*)

  val DeclareDutySuspendedDeliveriesJourney: List[ActionBuilder] =
    List[ActionBuilder](
      getAuthLoginPage,
      loginWithAuthLoginStub,
      getDeclareAlcoholDutyQuestion,
      postDeclareAlcoholDutyQuestion,
      getProductEntryGuidancePage,
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
      // postCheckYourAnswersDutySuspendedDeliveries
    )
  setup("declare-duty-suspended-deliveries-journey", "Declare Duty Suspended Deliveries Journey") withActions
    (DeclareDutySuspendedDeliveriesJourney: _*)

  val DeclareQuarterlySpiritsQuestionsJourney: List[ActionBuilder] =
    List[ActionBuilder](
      getAuthLoginPage,
      loginWithAuthLoginStub,
      getDeclareAlcoholDutyQuestion,
      postDeclareAlcoholDutyQuestion,
      getQuarterlySpiritsReturnsGuidancePage,
      getDeclareSpiritsTotalPage,
      postDeclareSpiritsTotal,
      getDeclareScotchWhiskyPage,
      postDeclareScotchWhisky,
      getDeclareIrishWhiskeyPage,
      postDeclareIrishWhiskey
    )
  setup("declare-quarterly-spirits-questions-journey", "Declare Quarterly Spirits Questions Journey") withActions
    (DeclareQuarterlySpiritsQuestionsJourney: _*)

  runSimulation()
}
