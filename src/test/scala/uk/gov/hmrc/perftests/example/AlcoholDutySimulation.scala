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
import uk.gov.hmrc.perftests.example.AdjustmentJourneyRequests.{getAdjustmentDutyValuePage, getAdjustmentListPage, getAdjustmentReturnDatePage, getAdjustmentTaxTypeCodePage, getAdjustmentTypePage, getAdjustmentVolumeWithSprPage, getAdjustmentVolumeWithoutSprPage, getAdjustmentsCheckYourAnswersPage, getDeclareAdjustmentQuestionPage, getNewSprDutyRatePage, getNewTaxTypeCodePage, getReasonForOverDeclarationPage, getReasonForUnderDeclarationPage, getRemoveAdjustmentPage, getSpoiltAlcoholTypePage, getSpoiltAlcoholVolumePage, postAdjustmentList, postAdjustmentReturnDate, postAdjustmentTaxTypeCode, postAdjustmentType, postAdjustmentVolumeWithSpr, postAdjustmentVolumeWithoutSpr, postAdjustmentsCheckYourAnswers, postDeclareAdjustmentQuestionPage, postNewSprDutyRate, postNewTaxTypeCode, postReasonForOverDeclarationPage, postReasonForUnderDeclarationPage, postRemoveAdjustmentPage, postSpoiltAlcoholTypePage, postSpoiltAlcoholVolume}
import uk.gov.hmrc.perftests.example.AlcoholDutyReturnsRequests._
import uk.gov.hmrc.perftests.example.DeclareDutySuspendedDeliveriesRequests._
import uk.gov.hmrc.perftests.example.DeclareQuarterlySpiritsQuestionsRequests._
import uk.gov.hmrc.perftests.example.ViewPastPaymentsRequests.{getViewPastPaymentsPage, postAuthLoginPageForViewPastPayments}
import uk.gov.hmrc.perftests.example.ViewPastReturnsRequests.{getViewPastReturnsPage, getViewSpecificReturnsPage, postAuthLoginPageForViewPastReturns}

class AlcoholDutySimulation extends PerformanceTestRunner {

  val randomAppaIds: Iterator[Map[String, String]] =
    Iterator.continually(Map("appaId" -> s"${generateUniqueReference(5)}0000100208"))
  def appaIdFeeder: ChainBuilder                   = feed(randomAppaIds)

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
    // RETURNS JOURNEY - BEER
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
    // RETURNS JOURNEY - CIDER
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
    // RETURNS JOURNEY - WINE
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
    // RETURNS JOURNEY - SPIRITS
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
    // RETURNS JOURNEY - OTHER FERMENTED PRODUCTS
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
    getTaskListPage,
    // ADJUSTMENT JOURNEY - UNDER DECLARATION
    getDeclareAdjustmentQuestionPage,
    postDeclareAdjustmentQuestionPage(),
    getAdjustmentTypePage,
    postAdjustmentType("under-declaration", false),
    getAdjustmentReturnDatePage("When should you have paid duty?"),
    postAdjustmentReturnDate,
    getAdjustmentTaxTypeCodePage("What is the tax type code for the alcohol you are adjusting?"),
    postAdjustmentTaxTypeCode(371),
    getAdjustmentVolumeWithSprPage,
    postAdjustmentVolumeWithSpr(),
    getAdjustmentDutyValuePage("£2,455.39"),
    getAdjustmentsCheckYourAnswersPage,
    postAdjustmentsCheckYourAnswers,
    getAdjustmentListPage,
    getRemoveAdjustmentPage,
    postRemoveAdjustmentPage(false),
    postAdjustmentList(),
    // ADJUSTMENT JOURNEY - OVER DECLARATION
    getAdjustmentTypePage,
    postAdjustmentType("over-declaration", false),
    getAdjustmentReturnDatePage("When did you pay duty on this?"),
    postAdjustmentReturnDate,
    getAdjustmentTaxTypeCodePage("What is the tax type code for the alcohol you are adjusting?"),
    postAdjustmentTaxTypeCode(371),
    getAdjustmentVolumeWithSprPage,
    postAdjustmentVolumeWithSpr(),
    getAdjustmentDutyValuePage("−£2,455.39"),
    getAdjustmentsCheckYourAnswersPage,
    postAdjustmentsCheckYourAnswers,
    getAdjustmentListPage,
    postAdjustmentList(),
    // ADJUSTMENT JOURNEY - SPOILT
    getAdjustmentTypePage,
    postAdjustmentType("spoilt"),
    getSpoiltAlcoholTypePage,
    postSpoiltAlcoholTypePage ("Spirits"),
    getSpoiltAlcoholVolumePage,
    postSpoiltAlcoholVolume,
    getAdjustmentsCheckYourAnswersPage,
    postAdjustmentsCheckYourAnswers,
    getAdjustmentListPage,
    postAdjustmentList(),
    // ADJUSTMENT JOURNEY - DRAWBACK
    getAdjustmentTypePage,
    postAdjustmentType("drawback", false),
    getAdjustmentReturnDatePage("When did you pay duty on this?"),
    postAdjustmentReturnDate,
    getAdjustmentTaxTypeCodePage("What is the tax type code for the alcohol you are adjusting?"),
    postAdjustmentTaxTypeCode(314, withSpr = false),
    getAdjustmentVolumeWithoutSprPage,
    postAdjustmentVolumeWithoutSpr,
    getAdjustmentDutyValuePage("−£2,322.59"),
    getAdjustmentsCheckYourAnswersPage,
    postAdjustmentsCheckYourAnswers,
    getAdjustmentListPage,
    postAdjustmentList(),
    // ADJUSTMENT JOURNEY - REPACKAGED
    getAdjustmentTypePage,
    postAdjustmentType("repackaged-draught-products", false),
    getAdjustmentReturnDatePage("When did you pay duty on this?"),
    postAdjustmentReturnDate,
    getAdjustmentTaxTypeCodePage("What is the original tax type code for the alcohol you are repackaging?"),
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
    getTaskListPage,
    getReasonForUnderDeclarationPage,
    postReasonForUnderDeclarationPage("Reason for under-declaration"),
    getReasonForOverDeclarationPage,
    postReasonForOverDeclarationPage("Reason for over-declaration"),
    // DSD JOURNEY
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
    getTaskListPage,
    // SPIRITS AND INGREDIENTS JOURNEY
    getQuarterlySpiritsReturnsGuidancePage,
    postQuarterlySpiritsReturnsGuidancePage(),
    getDeclareSpiritsTotalPage,
    postDeclareSpiritsTotal,
    getDeclareWhiskeyPage,
    postDeclareWhiskey,
    getWhichOfTheseSpiritsHaveYouProducedPage,
    postWhichOfTheseSpiritsHaveYouProduced,
    getOtherSpiritsProducedPage,
    postOtherSpiritsProduced,
    getQuarterlySpiritsCheckYourAnswersPage,
    getTaskListPage,
    // RETURN SUMMARY PAGES
    getReturnSummary("£63,157.99"),
    postReturnSummary,
    getReturnSubmitted
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
    // RETURNS JOURNEY - BEER
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
    getRemoveProductPage("Beer"),
    postRemoveProductPage(false, "Beer"),
    postMultipleSprListQuestionBeerPage(false),
    getCheckYourAnswersReturnsBeerPage,
    getDutyDueBeerPage("£10,717.14"),
    postDutyDueBeerPage,
    getTaskListPage,
    // RETURNS JOURNEY - CIDER
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
    getRemoveProductPage("Cider"),
    postRemoveProductPage(false, "Cider"),
    postMultipleSprListQuestionCiderPage(false),
    getCheckYourAnswersReturnsCiderPage,
    getDutyDueCiderPage("£4,403.82"),
    postDutyDueCiderPage,
    getTaskListPage,
    // RETURNS JOURNEY - WINE
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
    getRemoveProductPage("Wine"),
    postRemoveProductPage(false, "Wine"),
    postMultipleSprListQuestionWinePage(false),
    getCheckYourAnswersReturnsWinePage,
    getDutyDueWinePage("£11,008.73"),
    postDutyDueWinePage,
    getTaskListPage,
    // RETURNS JOURNEY - SPIRITS
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
    getRemoveProductPage("Spirits"),
    postRemoveProductPage(false, "Spirits"),
    postMultipleSprListQuestionSpiritsPage(false),
    getCheckYourAnswersReturnsSpiritsPage,
    getDutyDueSpiritsPage("£11,008.73"),
    postDutyDueSpiritsPage,
    getTaskListPage,
    // RETURNS JOURNEY - OTHER FERMENTED PRODUCTS
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
    getRemoveProductPage("OtherFermentedProduct"),
    postRemoveProductPage(false, "OtherFermentedProduct"),
    postMultipleSprListQuestionOtherFermentedProductPage(false),
    getCheckYourAnswersReturnsOtherFermentedProductPage,
    getDutyDueOtherFermentedProductPage("£11,008.73"),
    postDutyDueOtherFermentedProductPage,
    getTaskListPage,
    // ADJUSTMENT JOURNEY - WITH OPTION 'NO'
    getDeclareAdjustmentQuestionPage,
    postDeclareAdjustmentQuestionPage(false),
    getTaskListPage,
    // DSD JOURNEY - WITH OPTION 'NO'
    getDeclareDutySuspendedDeliveriesQuestion,
    postDeclareDutySuspendedDeliveriesQuestion(false),
    getTaskListPage,
    // SPIRITS AND INGREDIENTS JOURNEY - WITH OPTION 'NO'
    getQuarterlySpiritsReturnsGuidancePage,
    postQuarterlySpiritsReturnsGuidancePage(false),
    getTaskListPage,
    // RETURN SUMMARY PAGES
    getReturnSummary("£48,147.15 "),
    postReturnSummary,
    getReturnSubmitted
  )

  setup(
    "alcohol-duty-view-past-payments-journey",
    "Alcohol Duty View Past Payments"
  ) withRequests (
    getAuthLoginPage,
    postAuthLoginPageForViewPastPayments,
    getViewPastPaymentsPage
  )

  setup(
    "alcohol-duty-view-past-returns-journey",
    "Alcohol Duty View Past Returns"
  ) withRequests (
    getAuthLoginPage,
    postAuthLoginPageForViewPastReturns,
    getViewPastReturnsPage,
    getViewSpecificReturnsPage
  )

  runSimulation()
}
