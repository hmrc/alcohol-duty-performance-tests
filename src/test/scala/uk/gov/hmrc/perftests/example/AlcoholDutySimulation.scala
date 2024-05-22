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
import uk.gov.hmrc.perftests.example.AlcoholDutyReturnsRequests.{getPureAlcoholPage, _}
import uk.gov.hmrc.perftests.example.DeclareDutySuspendedDeliveriesRequests.{getCheckYourAnswersDutySuspendedDeliveries, _}
import uk.gov.hmrc.perftests.example.DeclareQuarterlySpiritsQuestionsRequests.{getDeclareIrishWhiskeyPage, getHowMuchRyeHaveYouUsedPage, _}

class AlcoholDutySimulation extends PerformanceTestRunner {

  val AlcoholDutyReturnsJourneyWithSPRandDRYes: List[ActionBuilder] =
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
      postProductVolumePage,
      getPureAlcoholPage,
      getProductDutyRatePage("£16.61")
    )

  setup(
    "alcohol-duty-returns-with-SPR-and-DR-Yes-journey",
    "Alcohol Duty Returns Journey When Draught Relief and Small Producer Relief Selected Yes"
  ) withActions
    (AlcoholDutyReturnsJourneyWithSPRandDRYes: _*)

  val AlcoholDutyReturnsJourneyWithSPRandDRNo: List[ActionBuilder] =
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
      postProductVolumePage,
      getPureAlcoholPage,
      getProductDutyRatePage("£34.52")
    )
  setup(
    "alcohol-duty-returns-with-SPR-and-DR-No-journey",
    "Alcohol Duty Returns Journey When Draught Relief and Small Producer Relief Selected No"
  ) withActions
    (AlcoholDutyReturnsJourneyWithSPRandDRNo: _*)

  val DeclareDutySuspendedDeliveriesJourney: List[ActionBuilder] =
    List[ActionBuilder](
      getAuthLoginPage,
      loginWithAuthLoginStub,
      getBeforeYouStartPage,
      getTaskListPage,
      postTaskListPage,
      getDeclareDutySuspendedDeliveriesQuestion,
      postDeclareDutySuspendedDeliveriesQuestion,
      getDutySuspendedDeliveriesGuidance,
      getDutySuspendedBeer,
      postDutySuspendedBeer,
      getDutySuspendedCider,
      postDutySuspendedCider,
      getDutySuspendedWine,
      postDutySuspendedWine,
      getDutySuspendedSpirits,
      postDutySuspendedSpirits,
      getDutySuspendedOtherFermentedProducts,
      postDutySuspendedOtherFermentedProducts,
      getCheckYourAnswersDutySuspendedDeliveries
        postCheckYourAnswersDutySuspendedDeliveries
        getTaskListPage,
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
      postDeclareIrishWhiskey,
      getWhichOfTheseSpiritsHaveYouMadePage,
      postWhichOfTheseSpiritsHaveYouMade,
      getHowMuchUnmaltedGrainHaveYouUsedPage,
      postHowMuchUnmaltedGrainHaveYouUsed,
      getHowMuchMaltedBarleyHaveYouUsedPage,
      postHowMuchMaltedBarleyHaveYouUsed,
      getHowMuchRyeHaveYouUsedPage,
      postHowMuchRyeHaveYouUsed,
      getHowMuchWheatHaveYouUsedPage,
      postHowMuchWheatHaveYouUsed
    )
  setup("declare-quarterly-spirits-questions-journey", "Declare Quarterly Spirits Questions Journey") withActions
    (DeclareQuarterlySpiritsQuestionsJourney: _*)

  runSimulation()
}
