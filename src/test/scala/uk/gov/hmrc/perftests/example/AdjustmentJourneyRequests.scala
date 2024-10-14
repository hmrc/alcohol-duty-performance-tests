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
import io.gatling.core.check.CheckBuilder
import io.gatling.core.check.regex.RegexCheckType
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object AdjustmentJourneyRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("alcohol-duty-returns-frontend")
  val route: String   = "manage-alcohol-duty"
  val CsrfPattern     = """<input type="hidden" name="csrfToken" value="([^"]+)""""

  def saveCsrfToken(): CheckBuilder[RegexCheckType, String, String] = regex(_ => CsrfPattern).saveAs("csrfToken")

  def getDeclareAdjustmentQuestionPage: HttpRequestBuilder =
    http("Get Declare Adjustment Question Page")
      .get(s"$baseUrl/$route/adjust-a-previous-return": String)
      .check(status.is(200))
      .check(regex("Do you need to declare an adjustment from a previously submitted return?"))

  def postDeclareAdjustmentQuestionPage(declareAdjustmentQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Declare Adjustment Question")
      .post(s"$baseUrl/$route/adjust-a-previous-return")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declare-adjustment-question-value", declareAdjustmentQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (declareAdjustmentQuestion) "type-of-adjustment"
        else "task-list/your-alcohol-duty-return"}": String))

  def getAdjustmentTypePage: HttpRequestBuilder =
    http("Get Adjustment Type Page")
      .get(s"$baseUrl/$route/type-of-adjustment": String)
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(200))
      .check(regex("What type of adjustment do you need to make?"))

  def postAdjustmentType(adjustmentType: String): HttpRequestBuilder =
    http("Post Adjustment Type")
      .post(s"$baseUrl/$route/type-of-adjustment")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("adjustment-type-value", adjustmentType)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/adjustment-return-date": String))

  def getAdjustmentReturnDatePage(pageHeader: String): HttpRequestBuilder =
    http("Get Adjustment Return Date Page")
      .get(s"$baseUrl/$route/adjustment-return-date": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex(pageHeader))

  def postAdjustmentReturnDate: HttpRequestBuilder =
    http("Post Adjustment Return Date")
      .post(s"$baseUrl/$route/adjustment-return-date")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("when-did-you-pay-duty-input.month", "09")
      .formParam("when-did-you-pay-duty-input.year", "2023")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/adjustment-tax-type-code": String))

  def getAdjustmentTaxTypeCodePage: HttpRequestBuilder =
    http("Get Adjustment Tax Type Code Page")
      .get(s"$baseUrl/$route/adjustment-tax-type-code": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What is the tax type code for the alcohol you are adjusting?"))

  def postAdjustmentTaxTypeCode(taxTypeCode: Int, withSpr: Boolean = true): HttpRequestBuilder =
    http("Post Adjustment Tax Type Code")
      .post(s"$baseUrl/$route/adjustment-tax-type-code")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("adjustment-tax-type-input", taxTypeCode)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (withSpr) "adjustment-volume-with-spr"
        else "adjustment-volume"}": String))

  def getAdjustmentVolumeWithSprPage: HttpRequestBuilder =
    http("Get Adjustment Volume With Spr Page")
      .get(s"$baseUrl/$route/adjustment-volume-with-spr": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much do you need to adjust?"))

  def postAdjustmentVolumeWithSpr(isRepackaged: Boolean = false): HttpRequestBuilder =
    http("Post Adjustment Volume With Spr")
      .post(s"$baseUrl/$route/adjustment-volume-with-spr")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumes.totalLitresVolume", 3000.75)
      .formParam("volumes.pureAlcoholVolume", 250.55)
      .formParam("volumes.sprDutyRate", 9.8)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (isRepackaged) "new-tax-type-code"
        else "adjustment-duty-value"}": String))

  def getAdjustmentVolumeWithoutSprPage: HttpRequestBuilder =
    http("Get Adjustment Volume Without Spr Page")
      .get(s"$baseUrl/$route/adjustment-volume": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much do you need to adjust?"))

  def postAdjustmentVolumeWithoutSpr: HttpRequestBuilder =
    http("Post Adjustment Volume Without Spr")
      .post(s"$baseUrl/$route/adjustment-volume")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumes.totalLitresVolume", 3000.75)
      .formParam("volumes.pureAlcoholVolume", 250.55)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/adjustment-duty-value": String))

  def getNewTaxTypeCodePage: HttpRequestBuilder =
    http("Get New Tax Type Code Page")
      .get(s"$baseUrl/$route/new-tax-type-code": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What is the tax type code for the newly repackaged products?"))

  def postNewTaxTypeCode: HttpRequestBuilder =
    http("Post New Tax Type Code")
      .post(s"$baseUrl/$route/new-tax-type-code")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("new-tax-type-code", 363)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/new-spr-duty-rate": String))

  def getNewSprDutyRatePage: HttpRequestBuilder =
    http("Get New Spr Duty Rate Page")
      .get(s"$baseUrl/$route/new-spr-duty-rate": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What is your Small Producer Relief Duty rate for the newly repackaged products?"))

  def postNewSprDutyRate: HttpRequestBuilder =
    http("Post New Spr Duty Rate")
      .post(s"$baseUrl/$route/new-spr-duty-rate")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("adjustment-small-producer-relief-duty-rate-input", 11.5)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/adjustment-duty-value": String))

  def getAdjustmentDutyValuePage(dutyDueAmount: String): HttpRequestBuilder =
    http("Get Adjustment Duty Value Page")
      .get(s"$baseUrl/$route/adjustment-duty-value": String)
      .check(status.is(200))
      .check(regex("The duty value for this adjustment is " + dutyDueAmount))

  def getAdjustmentsCheckYourAnswersPage: HttpRequestBuilder =
    http("Get Adjustments Check Your Answers Page")
      .get(s"$baseUrl/$route/adjustment-check-your-answers": String)
      .check(status.is(200))
      .check(regex("Check your answers"))

  def postAdjustmentsCheckYourAnswers: HttpRequestBuilder =
    http("Post Adjustments Check Your Answers")
      .post(s"$baseUrl/$route/adjustment-check-your-answers")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/adjustments-to-previous-returns/1": String))

  def getAdjustmentListPage: HttpRequestBuilder =
    http("Get Adjustment List Page")
      .get(s"$baseUrl/$route/adjustments-to-previous-returns/1": String)
      .check(status.is(200))
      .check(regex("Adjustments from previous returns"))

  def postAdjustmentList(anyOtherAdjustmentQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Adjustment List")
      .post(s"$baseUrl/$route/adjustments-to-previous-returns/1")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("adjustment-list-yes-no-value", anyOtherAdjustmentQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (anyOtherAdjustmentQuestion) "type-of-adjustment"
        else "task-list/your-alcohol-duty-return"}": String))

  def getRemoveAdjustmentPage: HttpRequestBuilder =
    http(s"Navigate to Remove this adjustment?")
      .get(s"$baseUrl/$route/remove-adjustment?index=0": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Remove this adjustment?"))

  def postRemoveAdjustmentPage(removeProduct: Boolean = true): HttpRequestBuilder =
    http(s"Post Remove this adjustment?")
      .post(s"$baseUrl/$route/remove-adjustment?index=0")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("delete-adjustment-yes-no-value", removeProduct)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/adjustments-to-previous-returns/1": String))
}
