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

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import uk.gov.hmrc.performance.simulation.PerformanceTestRunner
import uk.gov.hmrc.perftests.example.AdjustmentJourneyRequests.{getAdjustmentDutyValuePage, getAdjustmentListPage, getAdjustmentReturnDatePage, getAdjustmentTaxTypeCodePage, getAdjustmentTypePage, getAdjustmentVolumeWithSprPage, getAdjustmentVolumeWithoutSprPage, getAdjustmentsCheckYourAnswersPage, getDeclareAdjustmentQuestionPage, getNewSprDutyRatePage, getNewTaxTypeCodePage, postAdjustmentList, postAdjustmentReturnDate, postAdjustmentTaxTypeCode, postAdjustmentType, postAdjustmentVolumeWithSpr, postAdjustmentVolumeWithoutSpr, postAdjustmentsCheckYourAnswers, postDeclareAdjustmentQuestionPage, postNewSprDutyRate, postNewTaxTypeCode}
import uk.gov.hmrc.perftests.example.AlcoholDutyReturnsRequests._
import uk.gov.hmrc.perftests.example.DeclareDutySuspendedDeliveriesRequests.{getCheckYourAnswersDutySuspendedDeliveries, _}
import uk.gov.hmrc.perftests.example.DeclareQuarterlySpiritsQuestionsRequests.{postQuarterlySpiritsReturnsGuidancePage, _}

class AlcoholDutySimulation extends PerformanceTestRunner {

  val randomAppaIds: Iterator[Map[String, String]] =
    Iterator.continually(Map("appaId" -> s"${generateUniqueReference(5)}0000100208"))
  def appaIdFeeder: ChainBuilder = feed(randomAppaIds)

  setup(
    "alcohol-duty-returns-journey-with-multiple-spr-is-set-to-no",
    "Alcohol Duty Returns Journey When Multiple SPR is set to No"
  ) withActions (appaIdFeeder.actionBuilders: _*) withRequests (
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
    "alcohol-duty-returns-journey-with-multiple-spr-is-set-to-yes",
    "Alcohol Duty Returns Journey When Multiple SPR is set to YES"
  ) withActions (appaIdFeeder.actionBuilders: _*) withRequests (
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
    "declare-duty-suspended-deliveries-journey-with-option-yes",
    "Declare Duty Suspended Deliveries Journey With Option Yes"
  ) withActions (appaIdFeeder.actionBuilders: _*) withRequests (
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
    "declare-duty-suspended-deliveries-journey-with-option-no",
    "Declare Duty Suspended Deliveries Journey With Option No"
  ) withActions (appaIdFeeder.actionBuilders: _*) withRequests (
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
    "declare-quarterly-spirits-questions-journey-with-option-yes",
    "Declare Quarterly Spirits Questions Journey With Option Yes"
  ) withActions (appaIdFeeder.actionBuilders: _*) withRequests (
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
    "declare-quarterly-spirits-questions-journey-with-option-no",
    "Declare Quarterly Spirits Questions Journey With Option No"
  ) withActions (appaIdFeeder.actionBuilders: _*) withRequests (
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
    "declare-adjustments-journey-with-option-yes",
    "Declare Adjustments Journey With Option Yes"
  ) withActions (appaIdFeeder.actionBuilders: _*) withRequests(
    getAuthLoginPage,
    postAuthLoginPage,
    getBeforeYouStartPage,
    postBeforeYouStartPage,
    getTaskListPage,
    getDeclareAdjustmentQuestionPage,
    postDeclareAdjustmentQuestionPage(),
//    Under-Declaration Journey starts here
    getAdjustmentTypePage,
    postAdjustmentType("under-declaration"),
    getAdjustmentReturnDatePage("When should you have paid duty?"),
    postAdjustmentReturnDate,
    getAdjustmentTaxTypeCodePage,
    postAdjustmentTaxTypeCode(371),
    getAdjustmentVolumeWithSprPage,
    postAdjustmentVolumeWithSpr(),
    getAdjustmentDutyValuePage("£2,455.39"),
    getAdjustmentsCheckYourAnswersPage,
    postAdjustmentsCheckYourAnswers,
    getAdjustmentListPage,
    postAdjustmentList(),
//    Over-Declaration Journey starts here
    getAdjustmentTypePage,
    postAdjustmentType("over-declaration"),
    getAdjustmentReturnDatePage("When did you pay duty on this?"),
    postAdjustmentReturnDate,
    getAdjustmentTaxTypeCodePage,
    postAdjustmentTaxTypeCode(371),
    getAdjustmentVolumeWithSprPage,
    postAdjustmentVolumeWithSpr(),
    getAdjustmentDutyValuePage("−£2,455.39"),
    getAdjustmentsCheckYourAnswersPage,
    postAdjustmentsCheckYourAnswers,
    getAdjustmentListPage,
    postAdjustmentList(),
//    Spoilt Journey starts here
    getAdjustmentTypePage,
    postAdjustmentType("spoilt"),
    getAdjustmentReturnDatePage("To the nearest month, when did you pay duty on this?"),
    postAdjustmentReturnDate,
    getAdjustmentTaxTypeCodePage,
    postAdjustmentTaxTypeCode(312,false),
    getAdjustmentVolumeWithoutSprPage,
    postAdjustmentVolumeWithoutSpr,
    getAdjustmentDutyValuePage("−£2,322.59"),
    getAdjustmentsCheckYourAnswersPage,
    postAdjustmentsCheckYourAnswers,
    getAdjustmentListPage,
    postAdjustmentList(),
//    Drawback Journey starts here
    getAdjustmentTypePage,
    postAdjustmentType("drawback"),
    getAdjustmentReturnDatePage("When did you pay duty on this?"),
    postAdjustmentReturnDate,
    getAdjustmentTaxTypeCodePage,
    postAdjustmentTaxTypeCode(314,false),
    getAdjustmentVolumeWithoutSprPage,
    postAdjustmentVolumeWithoutSpr,
    getAdjustmentDutyValuePage("−£2,322.59"),
    getAdjustmentsCheckYourAnswersPage,
    postAdjustmentsCheckYourAnswers,
    getAdjustmentListPage,
    postAdjustmentList(),
//    Repackaged Journey starts here
    getAdjustmentTypePage,
    postAdjustmentType("repackaged-draught-products"),
    getAdjustmentReturnDatePage("When did you pay duty on this?"),
    postAdjustmentReturnDate,
    getAdjustmentTaxTypeCodePage,
    postAdjustmentTaxTypeCode(373),
    getAdjustmentVolumeWithSprPage,
    postAdjustmentVolumeWithSpr(true),
    getNewTaxTypeCodePage,
    postNewTaxTypeCode,
    getNewSprDutyRatePage,
    postNewSprDutyRate,
    getAdjustmentDutyValuePage("£425.93"),
    getAdjustmentsCheckYourAnswersPage,
    postAdjustmentsCheckYourAnswers,
    getAdjustmentListPage,
    postAdjustmentList(false),
    getTaskListPage
  )

  setup(
    "declare-adjustments-journey-with-option-no",
    "Declare Adjustments Journey With Option No"
  ) withActions (appaIdFeeder.actionBuilders: _*) withRequests(
    getAuthLoginPage,
    postAuthLoginPage,
    getBeforeYouStartPage,
    postBeforeYouStartPage,
    getTaskListPage,
    getDeclareAdjustmentQuestionPage,
    postDeclareAdjustmentQuestionPage(false),
    getTaskListPage
  )

  runSimulation()
}
