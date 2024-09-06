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
import uk.gov.hmrc.perftests.example.DeclareDutySuspendedDeliveriesRequests.{getCheckYourAnswersDutySuspendedDeliveries, _}
import uk.gov.hmrc.perftests.example.DeclareQuarterlySpiritsQuestionsRequests.{postQuarterlySpiritsReturnsGuidancePage, _}

class AlcoholDutySimulation extends PerformanceTestRunner {

  val AlcoholDutyReturnsJourneyWithMultipleSPRisSetToNo: List[ActionBuilder] =
    List[ActionBuilder](
      getClearData,
      getAuthLoginPage,
      postAuthLoginPage,
      getBeforeYouStartPage,
      postBeforeYouStartPage,
      getTaskListPage,
      getDeclareAlcoholDutyQuestion,
      postDeclareAlcoholDutyQuestion,
      getAlcoholTypesToDeclare,
      postAlcoholTypesToDeclare,
      getWhatDoYouNeedToDeclareBeerPage,
      postWhatDoYouNeedToDeclareBeerPage,
      getHowMuchYouNeedToDeclareBeerPage,
      postHowMuchYouNeedToDeclareBeerPage,
      getDoYouHaveMultipleSprDutyRateBeerPage,
      postDoYouHaveMultipleSprDutyRateBeerPage,
      getSingleSprRateBeerPage,
      postSingleSprRateBeerPage,
      getCheckYourAnswersReturnsBeerPage,
      getDutyDueBeerPage,
      postDutyDueBeerPage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareCiderPage,
      postWhatDoYouNeedToDeclareCiderPage,
      getHowMuchYouNeedToDeclareCiderPage,
      postHowMuchYouNeedToDeclareCiderPage,
      getDoYouHaveMultipleSprDutyRateCiderPage,
      postDoYouHaveMultipleSprDutyRateCiderPage,
      getSingleSprRateCiderPage,
      postSingleSprRateCiderPage,
      getCheckYourAnswersReturnsCiderPage,
      getDutyDueCiderPage,
      postDutyDueCiderPage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareWinePage,
      postWhatDoYouNeedToDeclareWinePage,
      getHowMuchYouNeedToDeclareWinePage,
      postHowMuchYouNeedToDeclareWinePage,
      getDoYouHaveMultipleSprDutyRateWinePage,
      postDoYouHaveMultipleSprDutyRateWinePage,
      getSingleSprRateWinePage,
      postSingleSprRateWinePage,
      getCheckYourAnswersReturnsWinePage,
      getDutyDueWinePage,
      postDutyDueWinePage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareSpiritsPage,
      postWhatDoYouNeedToDeclareSpiritsPage,
      getHowMuchYouNeedToDeclareSpiritsPage,
      postHowMuchYouNeedToDeclareSpiritsPage,
      getDoYouHaveMultipleSprDutyRateSpiritsPage,
      postDoYouHaveMultipleSprDutyRateSpiritsPage,
      getSingleSprRateSpiritsPage,
      postSingleSprRateSpiritsPage,
      getCheckYourAnswersReturnsSpiritsPage,
      getDutyDueSpiritsPage,
      postDutyDueSpiritsPage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareOtherFermentedProductPage,
      postWhatDoYouNeedToDeclareOtherFermentedProductPage,
      getHowMuchYouNeedToDeclareOtherFermentedProductPage,
      postHowMuchYouNeedToDeclareOtherFermentedProductPage,
      getDoYouHaveMultipleSprDutyRateOtherFermentedProductPage,
      postDoYouHaveMultipleSprDutyRateOtherFermentedProductPage,
      getSingleSprRateOtherFermentedProductPage,
      postSingleSprRateOtherFermentedProductPage,
      getCheckYourAnswersReturnsOtherFermentedProductPage,
      getDutyDueOtherFermentedProductPage,
      postDutyDueOtherFermentedProductPage,
      getTaskListPage
    )
  setup(
    "alcohol-duty-returns-journey-with-multiple-spr-is-set-to-no",
    "Alcohol Duty Returns Journey When Multiple SPR is set to No"
  ) withActions
    (AlcoholDutyReturnsJourneyWithMultipleSPRisSetToNo: _*)

  val DeclareDutySuspendedDeliveriesJourneyWithOptionYes: List[ActionBuilder] =
    List[ActionBuilder](
      getClearData,
      getAuthLoginPage,
      postAuthLoginPage,
      getBeforeYouStartPage,
      postBeforeYouStartPage,
      getTaskListPage,
      getDeclareDutySuspendedDeliveriesQuestion,
      postDeclareDutySuspendedDeliveriesQuestion(),
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
      getCheckYourAnswersDutySuspendedDeliveries,
      getTaskListPage
    )
  setup("declare-duty-suspended-deliveries-journey-with-option-yes", "Declare Duty Suspended Deliveries Journey With Option Yes") withActions
    (DeclareDutySuspendedDeliveriesJourneyWithOptionYes: _*)

  val DeclareDutySuspendedDeliveriesJourneyWithOptionNo: List[ActionBuilder] =
    List[ActionBuilder](
      getClearData,
      getAuthLoginPage,
      postAuthLoginPage,
      getBeforeYouStartPage,
      postBeforeYouStartPage,
      getTaskListPage,
      getDeclareDutySuspendedDeliveriesQuestion,
      postDeclareDutySuspendedDeliveriesQuestion(false),
      getTaskListPage
    )
  setup("declare-duty-suspended-deliveries-journey-with-option-no", "Declare Duty Suspended Deliveries Journey With Option No") withActions
    (DeclareDutySuspendedDeliveriesJourneyWithOptionNo: _*)

  val DeclareQuarterlySpiritsQuestionsJourneyWithOptionYes: List[ActionBuilder] =
    List[ActionBuilder](
      getClearData,
      getAuthLoginPage,
      postAuthLoginPage,
      getBeforeYouStartPage,
      postBeforeYouStartPage,
      getTaskListPage,
      getQuarterlySpiritsReturnsGuidancePage,
      postQuarterlySpiritsReturnsGuidancePage(),
      getDeclareSpiritsTotalPage,
      postDeclareSpiritsTotal,
      getDeclareWhiskeyPage,
      postDeclareWhiskey,
      getWhichOfTheseSpiritsHaveYouMadePage,
      postWhichOfTheseSpiritsHaveYouMade,
      getOtherSpiritsProducedPage,
      postOtherSpiritsProduced,
      getHowMuchGrainhaveYouUsedPage,
      postHowMuchGrainhaveYouUsed(),
      getOtherMaltedGrainsPage,
      postOtherMaltedGrains,
      getHowMuchAlcoholHaveYouUsedPage,
      postHowMuchAlcoholHaveYouUsed,
      getDeclareEthyleneGasOrMolassesPage,
      postDeclareEthyleneGasOrMolasses(),
      getDeclareOtherIngredientsPage,
      postDeclareOtherIngredients,
      getQuarterlySpiritsCheckYourAnswersPage,
      getTaskListPage
    )
  setup("declare-quarterly-spirits-questions-journey-with-option-yes", "Declare Quarterly Spirits Questions Journey With Option Yes") withActions
    (DeclareQuarterlySpiritsQuestionsJourneyWithOptionYes: _*)

  val DeclareQuarterlySpiritsQuestionsJourneyWithOptionNo: List[ActionBuilder] =
    List[ActionBuilder](
      getClearData,
      getAuthLoginPage,
      postAuthLoginPage,
      getBeforeYouStartPage,
      postBeforeYouStartPage,
      getTaskListPage,
      getQuarterlySpiritsReturnsGuidancePage,
      postQuarterlySpiritsReturnsGuidancePage(false),
      getTaskListPage
    )
  setup("declare-quarterly-spirits-questions-journey-with-option-no", "Declare Quarterly Spirits Questions Journey With Option No") withActions
    (DeclareQuarterlySpiritsQuestionsJourneyWithOptionNo: _*)

  runSimulation()
}
