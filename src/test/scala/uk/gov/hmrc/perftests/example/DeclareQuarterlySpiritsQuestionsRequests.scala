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

import com.fasterxml.jackson.databind.introspect.VisibilityChecker
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

  def getWhichOfTheseSpiritsHaveYouMadePage: HttpRequestBuilder =
    http("Get Which Of These Spirits Have You Made Page")
      .get(s"$baseUrl/$route/which-of-these-spirits-have-you-made": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex(" Which of these spirits have you made?"))

  def postWhichOfTheseSpiritsHaveYouMade: HttpRequestBuilder =
    http("Post Which Of These Spirits Have You Made")
      .post(s"$baseUrl/$route/which-of-these-spirits-have-you-made")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value[0]", "maltSpirits")
      .formParam("value[2]", "neutralAgriculturalOrigin")
      .formParam("value[4]", "beer")
      .check(status.is(303))

  def getHowMuchUnmaltedGrainHaveYouUsedPage: HttpRequestBuilder =
    http("Get How Much Unmalted Grain Have You Used Page")
      .get(s"$baseUrl/$route/how-much-unmalted-grain-have-you-used": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much unmalted grain have you used?"))

  def postHowMuchUnmaltedGrainHaveYouUsed: HttpRequestBuilder =
    http("Post How Much Unmalted Grain Have You Used")
      .post(s"$baseUrl/$route/how-much-unmalted-grain-have-you-used")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("unmalted-grain-used-input", 75)
      .check(status.is(303))
//      .check(header("Location").is(s"/$route/declareIrishWhiskey": String))

  def getHowMuchMaltedBarleyHaveYouUsedPage: HttpRequestBuilder =
    http("Get How Much Malted Barley Have You Used Page")
      .get(s"$baseUrl/$route/how-much-malted-barley-have-you-used": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much malted barley have you used?"))

  def postHowMuchMaltedBarleyHaveYouUsed: HttpRequestBuilder =
    http("Post How Much Malted Barley Have You Used Used")
      .post(s"$baseUrl/$route/how-much-malted-barley-have-you-used")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("malted-barley-used-input", 75)
      .check(status.is(303))
//  .check (header ("Location").is (s"/$route/declareIrishWhiskey": String) )

  def getHowMuchRyeHaveYouUsedPage: HttpRequestBuilder =
    http("Get How Much Rye Have You Used Page")
      .get(s"$baseUrl/$route/how-much-rye-have-you-used": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much rye have you used?"))

  def postHowMuchRyeHaveYouUsed: HttpRequestBuilder =
    http("Post How Much Rye Have You Used Used")
      .post(s"$baseUrl/$route/how-much-rye-have-you-used")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("rye-used-input", 75)
      .check(status.is(303))
//      .check(header("Location").is(s"/$route/declareIrishWhiskey": String))
}
