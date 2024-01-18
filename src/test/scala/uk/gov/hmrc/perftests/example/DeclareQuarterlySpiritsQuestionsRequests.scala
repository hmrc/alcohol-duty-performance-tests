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
      .get(s"$baseUrl/$route/quarterlySpiritsReturnGuidance": String)
      .check(status.is(200))
      .check(regex("Tell us about your ingredients and spirits"))

  def getDeclareSpiritsTotalPage: HttpRequestBuilder =
    http("Get Declare Spirits Total Page")
      .get(s"$baseUrl/$route/declareSpiritsTotal": String)
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(200))
      .check(regex("What is the total volume of spirits you made this quarter?"))

  def postDeclareSpiritsTotal: HttpRequestBuilder =
    http("Post Declare Spirits Total")
      .post(s"$baseUrl/$route/declareSpiritsTotal")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declare-spirits-total-input", 35)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/declareScotchWhisky": String))

  def getDeclareScotchWhiskyPage: HttpRequestBuilder =
    http("Get Declare Scotch Whisky Page")
      .get(s"$baseUrl/$route/declareScotchWhisky": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much Scotch Whisky have you made?"))

  def postDeclareScotchWhisky: HttpRequestBuilder =
    http("Post Declare Scotch Whisky")
      .post(s"$baseUrl/$route/declareScotchWhisky")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declare-scotch-whisky-input", 11)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/declareIrishWhiskey": String))

  def getDeclareIrishWhiskeyPage: HttpRequestBuilder =
    http("Get Declare Irish Whiskey Page")
      .get(s"$baseUrl/$route/declareIrishWhiskey": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much Irish Whiskey have you made?"))

  def postDeclareIrishWhiskey: HttpRequestBuilder =
    http("Post Declare Irish Whiskey")
      .post(s"$baseUrl/$route/declareIrishWhiskey")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declare-irish-whiskey-input", 13)
      .check(status.is(303))
}
