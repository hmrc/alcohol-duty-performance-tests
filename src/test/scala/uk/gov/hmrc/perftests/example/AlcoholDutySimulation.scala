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
      postDeclareAlcoholDutyQuestion(),
      getAlcoholTypesToDeclare,
      postAlcoholTypesToDeclare,
      getWhatDoYouNeedToDeclareBeerPage,
      postWhatDoYouNeedToDeclareBeerPage,
      getHowMuchYouNeedToDeclareBeerPage,
      postHowMuchYouNeedToDeclareBeerPage,
      getDoYouHaveMultipleSprDutyRateBeerPage,
      postDoYouHaveMultipleSprDutyRateBeerPage(false),
      getSingleSprRateBeerPage,
      postSingleSprRateBeerPage,
      getCheckYourAnswersReturnsBeerPage,
      getDutyDueBeerPage("£14,749.75"),
      postDutyDueBeerPage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareCiderPage,
      postWhatDoYouNeedToDeclareCiderPage,
      getHowMuchYouNeedToDeclareCiderPage,
      postHowMuchYouNeedToDeclareCiderPage,
      getDoYouHaveMultipleSprDutyRateCiderPage,
      postDoYouHaveMultipleSprDutyRateCiderPage(false),
      getSingleSprRateCiderPage,
      postSingleSprRateCiderPage,
      getCheckYourAnswersReturnsCiderPage,
      getDutyDueCiderPage("£8,436.43"),
      postDutyDueCiderPage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareWinePage,
      postWhatDoYouNeedToDeclareWinePage,
      getHowMuchYouNeedToDeclareWinePage,
      postHowMuchYouNeedToDeclareWinePage,
      getDoYouHaveMultipleSprDutyRateWinePage,
      postDoYouHaveMultipleSprDutyRateWinePage(false),
      getSingleSprRateWinePage,
      postSingleSprRateWinePage,
      getCheckYourAnswersReturnsWinePage,
      getDutyDueWinePage("£15,041.34"),
      postDutyDueWinePage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareSpiritsPage,
      postWhatDoYouNeedToDeclareSpiritsPage,
      getHowMuchYouNeedToDeclareSpiritsPage,
      postHowMuchYouNeedToDeclareSpiritsPage,
      getDoYouHaveMultipleSprDutyRateSpiritsPage,
      postDoYouHaveMultipleSprDutyRateSpiritsPage(false),
      getSingleSprRateSpiritsPage,
      postSingleSprRateSpiritsPage,
      getCheckYourAnswersReturnsSpiritsPage,
      getDutyDueSpiritsPage("£15,041.34"),
      postDutyDueSpiritsPage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareOtherFermentedProductPage,
      postWhatDoYouNeedToDeclareOtherFermentedProductPage,
      getHowMuchYouNeedToDeclareOtherFermentedProductPage,
      postHowMuchYouNeedToDeclareOtherFermentedProductPage,
      getDoYouHaveMultipleSprDutyRateOtherFermentedProductPage,
      postDoYouHaveMultipleSprDutyRateOtherFermentedProductPage(false),
      getSingleSprRateOtherFermentedProductPage,
      postSingleSprRateOtherFermentedProductPage,
      getCheckYourAnswersReturnsOtherFermentedProductPage,
      getDutyDueOtherFermentedProductPage("£15,041.34"),
      postDutyDueOtherFermentedProductPage,
      getTaskListPage
    )
  setup(
    "alcohol-duty-returns-journey-with-multiple-spr-is-set-to-no",
    "Alcohol Duty Returns Journey When Multiple SPR is set to No"
  ) withActions
    (AlcoholDutyReturnsJourneyWithMultipleSPRisSetToNo: _*)

  val AlcoholDutyReturnsJourneyWithMultipleSPRisSetToYes: List[ActionBuilder] =
    List[ActionBuilder](
      getClearData,
      getAuthLoginPage,
      postAuthLoginPage,
      getBeforeYouStartPage,
      postBeforeYouStartPage,
      getTaskListPage,
      getDeclareAlcoholDutyQuestion,
      postDeclareAlcoholDutyQuestion(),
      getAlcoholTypesToDeclare,
      postAlcoholTypesToDeclare,
      getWhatDoYouNeedToDeclareBeerPage,
      postWhatDoYouNeedToDeclareBeerPage,
      getHowMuchYouNeedToDeclareBeerPage,
      postHowMuchYouNeedToDeclareBeerPage,
      postDoYouHaveMultipleSprDutyRateBeerPage(),
      getMultipleSprRateBeerPage,
      postMultipleSprRateBeerPage,
      getCheckYourAnswersSprBeerPage,
      postCheckYourAnswersSprBeerPage,
      getMultipleSprListQuestionBeerPage,
      postMultipleSprListQuestionBeerPage(false),
      getCheckYourAnswersReturnsBeerPage,
      getDutyDueBeerPage("£10,717.14"),
      postDutyDueBeerPage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareCiderPage,
      postWhatDoYouNeedToDeclareCiderPage,
      getHowMuchYouNeedToDeclareCiderPage,
      postHowMuchYouNeedToDeclareCiderPage,
      postDoYouHaveMultipleSprDutyRateCiderPage(),
      getMultipleSprRateCiderPage,
      postMultipleSprRateCiderPage,
      getCheckYourAnswersSprCiderPage,
      postCheckYourAnswersSprCiderPage,
      getMultipleSprListQuestionCiderPage,
      postMultipleSprListQuestionCiderPage(false),
      getCheckYourAnswersReturnsCiderPage,
      getDutyDueCiderPage("£4,403.82"),
      postDutyDueCiderPage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareWinePage,
      postWhatDoYouNeedToDeclareWinePage,
      getHowMuchYouNeedToDeclareWinePage,
      postHowMuchYouNeedToDeclareWinePage,
      postDoYouHaveMultipleSprDutyRateWinePage(),
      getMultipleSprRateWinePage,
      postMultipleSprRateWinePage,
      getCheckYourAnswersSprWinePage,
      postCheckYourAnswersSprWinePage,
      getMultipleSprListQuestionWinePage,
      postMultipleSprListQuestionWinePage(false),
      getCheckYourAnswersReturnsWinePage,
      getDutyDueWinePage("£11,008.73"),
      postDutyDueWinePage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareSpiritsPage,
      postWhatDoYouNeedToDeclareSpiritsPage,
      getHowMuchYouNeedToDeclareSpiritsPage,
      postHowMuchYouNeedToDeclareSpiritsPage,
      postDoYouHaveMultipleSprDutyRateSpiritsPage(),
      getMultipleSprRateSpiritsPage,
      postMultipleSprRateSpiritsPage,
      getCheckYourAnswersSprSpiritsPage,
      postCheckYourAnswersSprSpiritsPage,
      getMultipleSprListQuestionSpiritsPage,
      postMultipleSprListQuestionSpiritsPage(false),
      getCheckYourAnswersReturnsSpiritsPage,
      getDutyDueSpiritsPage("£11,008.73"),
      postDutyDueSpiritsPage,
      getTaskListPage,
      getWhatDoYouNeedToDeclareOtherFermentedProductPage,
      postWhatDoYouNeedToDeclareOtherFermentedProductPage,
      getHowMuchYouNeedToDeclareOtherFermentedProductPage,
      postHowMuchYouNeedToDeclareOtherFermentedProductPage,
      postDoYouHaveMultipleSprDutyRateOtherFermentedProductPage(),
      getMultipleSprRateOtherFermentedProductPage,
      postMultipleSprRateOtherFermentedProductPage,
      getCheckYourAnswersSprOtherFermentedProductPage,
      postCheckYourAnswersSprOtherFermentedProductPage,
      getMultipleSprListQuestionOtherFermentedProductPage,
      postMultipleSprListQuestionOtherFermentedProductPage(false),
      getCheckYourAnswersReturnsOtherFermentedProductPage,
      getDutyDueOtherFermentedProductPage("£11,008.73"),
      postDutyDueOtherFermentedProductPage,
      getTaskListPage
    )
  setup(
    "alcohol-duty-returns-journey-with-multiple-spr-is-set-to-yes",
    "Alcohol Duty Returns Journey When Multiple SPR is set to YES"
  ) withActions
    (AlcoholDutyReturnsJourneyWithMultipleSPRisSetToYes: _*)

  val DeclareDutySuspendedDeliveriesJourneyWithOptionYes: List[ActionBuilder] =
    List[ActionBuilder](
//      getClearData,
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
  setup(
    "declare-duty-suspended-deliveries-journey-with-option-yes",
    "Declare Duty Suspended Deliveries Journey With Option Yes"
  ) withActions
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
  setup(
    "declare-duty-suspended-deliveries-journey-with-option-no",
    "Declare Duty Suspended Deliveries Journey With Option No"
  ) withActions
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
  setup(
    "declare-quarterly-spirits-questions-journey-with-option-yes",
    "Declare Quarterly Spirits Questions Journey With Option Yes"
  ) withActions
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
  setup(
    "declare-quarterly-spirits-questions-journey-with-option-no",
    "Declare Quarterly Spirits Questions Journey With Option No"
  ) withActions
    (DeclareQuarterlySpiritsQuestionsJourneyWithOptionNo: _*)

  runSimulation()
}
