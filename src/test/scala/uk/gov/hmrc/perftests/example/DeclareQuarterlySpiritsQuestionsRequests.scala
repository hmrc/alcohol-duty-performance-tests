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

object DeclareQuarterlySpiritsQuestionsRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("alcohol-duty-returns-frontend")
  val route: String   = "manage-alcohol-duty"
  val CsrfPattern     = """<input type="hidden" name="csrfToken" value="([^"]+)""""

  def saveCsrfToken(): CheckBuilder[RegexCheckType, String, String] = regex(_ => CsrfPattern).saveAs("csrfToken")

  def getQuarterlySpiritsReturnsGuidancePage: HttpRequestBuilder =
    http("Get Quarterly Spirits Returns Guidance Page")
      .get(s"$baseUrl/$route/complete-return/quarterly-spirits-production/have-you-produced-spirits": String)
      .check(status.is(200))
      .check(regex("Tell us about the spirits you have produced from raw ingredients"))

  def postQuarterlySpiritsReturnsGuidancePage(declareQSRQuestion: Boolean = true): HttpRequestBuilder = {
    http("Post Quarterly Spirits Returns Guidance")
      .post(s"$baseUrl/$route/complete-return/quarterly-spirits-production/have-you-produced-spirits")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declareQuarterlySpirits-yesNoValue", declareQSRQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${
        if (declareQSRQuestion) "complete-return/quarterly-spirits-production/report/total-volume"
        else "complete-return/task-list"
      }": String))
  }

  def getDeclareSpiritsTotalPage: HttpRequestBuilder =
    http("Get Declare Spirits Total Page")
      .get(s"$baseUrl/$route/complete-return/quarterly-spirits-production/report/total-volume": String)
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(200))
      .check(regex("What is the total of all spirits taken this quarter?"))

  def postDeclareSpiritsTotal: HttpRequestBuilder =
    http("Post Declare Spirits Total")
      .post(s"$baseUrl/$route/complete-return/quarterly-spirits-production/report/total-volume")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declare-spirits-total-input", 35)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/quarterly-spirits-production/report/scotch-whisky-and-irish-whiskey-volumes": String))


  def getDeclareWhiskeyPage: HttpRequestBuilder =
    http("Get Declare Whiskey Page")
      .get(s"$baseUrl/$route/complete-return/quarterly-spirits-production/report/scotch-whisky-and-irish-whiskey-volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much Scotch Whisky and Irish Whiskey have you produced?"))

  def postDeclareWhiskey: HttpRequestBuilder =
    http("Post Declare Whiskey")
      .post(s"$baseUrl/$route/complete-return/quarterly-spirits-production/report/scotch-whisky-and-irish-whiskey-volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("scotchWhisky", 11)
      .formParam("irishWhiskey", 11)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/quarterly-spirits-production/report/spirits-produced": String))

  def getWhichOfTheseSpiritsHaveYouProducedPage: HttpRequestBuilder =
    http("Get Which Of These Spirits Have You Produced Page")
      .get(s"$baseUrl/$route/complete-return/quarterly-spirits-production/report/spirits-produced": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Which of these spirits have you produced?"))

  def postWhichOfTheseSpiritsHaveYouProduced: HttpRequestBuilder =
    http("Post Which Of These Spirits Have You Produced")
      .post(s"$baseUrl/$route/complete-return/quarterly-spirits-production/report/spirits-produced")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value[0]", "maltSpirits")
      .formParam("value[1]", "grainSpirits")
      .formParam("value[2]", "neutralAgriculturalOrigin")
      .formParam("value[3]", "neutralIndustrialOrigin")
      .formParam("value[4]", "beer")
      .formParam("value[5]", "wineOrMadeWine")
      .formParam("value[6]", "ciderOrPerry")
      .formParam("value[7]", "other")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/quarterly-spirits-production/report/other-spirits-produced": String))

  def getOtherSpiritsProducedPage: HttpRequestBuilder =
    http("Get Other Spirits Produced Page")
      .get(s"$baseUrl/$route/complete-return/quarterly-spirits-production/report/other-spirits-produced": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Which other types of spirits have you produced?"))

  def postOtherSpiritsProduced: HttpRequestBuilder =
    http("Post Other Spirits Produced")
      .post(s"$baseUrl/$route/complete-return/quarterly-spirits-production/report/other-spirits-produced")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("otherSpiritsProduced", "Test Spirits")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/quarterly-spirits-production/check-your-answers": String))

  def getQuarterlySpiritsCheckYourAnswersPage: HttpRequestBuilder =
    http("Get Quarterly Spirits Check Your Answers Page")
      .get(s"$baseUrl/$route/complete-return/quarterly-spirits-production/check-your-answers": String)
      .check(status.is(200))
      .check(regex("Check your answers"))
}
