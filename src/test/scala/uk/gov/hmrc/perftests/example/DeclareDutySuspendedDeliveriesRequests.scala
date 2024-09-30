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
import uk.gov.hmrc.perftests.example.AlcoholDutyReturnsRequests.periodKey

object DeclareDutySuspendedDeliveriesRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("alcohol-duty-returns-frontend")
  val route: String   = "manage-alcohol-duty"
  val CsrfPattern     = """<input type="hidden" name="csrfToken" value="([^"]+)""""

  def saveCsrfToken(): CheckBuilder[RegexCheckType, String, String] = regex(_ => CsrfPattern).saveAs("csrfToken")

  def getBeforeYouStartPage: HttpRequestBuilder =
    http("Get Before You Start Page")
      .get(s"$baseUrl/$route/before-you-start-your-return/" + periodKey: String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Before you start"))

  def postBeforeYouStartPage: HttpRequestBuilder =
    http("Post Before You Start Page")
      .post(s"$baseUrl/$route/before-you-start-your-return": String)
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/task-list/your-alcohol-duty-return": String))

  def getTaskListPage: HttpRequestBuilder =
    http("Get Task List Page")
      .get(s"$baseUrl/$route/task-list/your-alcohol-duty-return": String)
      .check(status.is(200))
      .check(regex("Complete your Alcohol Duty Return"))

  def getDeclareDutySuspendedDeliveriesQuestion: HttpRequestBuilder =
    http("Get Declare Duty Suspended Deliveries Question Page")
      .get(s"$baseUrl/$route/do-you-need-to-declare-delivered-received-duty-suspended": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Do you need to tell us about any duty suspended alcohol?"))

  def postDeclareDutySuspendedDeliveriesQuestion(declareDSDQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Declare Duty Suspended Deliveries Question")
      .post(s"$baseUrl/$route/do-you-need-to-declare-delivered-received-duty-suspended")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declare-duty-suspended-deliveries-input", declareDSDQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (declareDSDQuestion) "tell-us-about-your-duty-suspended-deliveries"
        else "task-list/your-alcohol-duty-return"}": String))

  def getDutySuspendedDeliveriesGuidance: HttpRequestBuilder =
    http("Get Duty Suspended Deliveries Guidance")
      .get(s"$baseUrl/$route/tell-us-about-your-duty-suspended-deliveries": String)
      .check(status.is(200))
      .check(regex("Calculate your duty suspended alcohol"))

  def getDutySuspendedBeer: HttpRequestBuilder =
    http("Get Duty Suspended Beer")
      .get(s"$baseUrl/$route/tell-us-about-your-beer-in-duty-suspense": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about your duty suspended beer"))

  def postDutySuspendedBeer: HttpRequestBuilder =
    http("Post Duty Suspended Beer")
      .post(s"$baseUrl/$route/tell-us-about-your-beer-in-duty-suspense")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("totalBeer", 406.45)
      .formParam("pureAlcoholInBeer", 40.55)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/tell-us-about-your-cider-in-duty-suspense": String))

  def getDutySuspendedCider: HttpRequestBuilder =
    http("Get Duty Suspended Cider")
      .get(s"$baseUrl/$route/tell-us-about-your-cider-in-duty-suspense": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about your duty suspended cider"))

  def postDutySuspendedCider: HttpRequestBuilder =
    http("Post Duty Suspended Cider")
      .post(s"$baseUrl/$route/tell-us-about-your-cider-in-duty-suspense")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("totalCider", 306.45)
      .formParam("pureAlcoholInCider", 30.55)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/tell-us-about-your-wine-in-duty-suspense": String))

  def getDutySuspendedWine: HttpRequestBuilder =
    http("Get Duty Suspended Wine")
      .get(s"$baseUrl/$route/tell-us-about-your-wine-in-duty-suspense": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about your duty suspended wine"))

  def postDutySuspendedWine: HttpRequestBuilder =
    http("Post Duty Suspended Wine")
      .post(s"$baseUrl/$route/tell-us-about-your-wine-in-duty-suspense")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("totalWine", 506.45)
      .formParam("pureAlcoholInWine", 50.55)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/tell-us-about-your-spirits-in-duty-suspense": String))

  def getDutySuspendedSpirits: HttpRequestBuilder =
    http("Get Duty Suspended Spirits")
      .get(s"$baseUrl/$route/tell-us-about-your-spirits-in-duty-suspense": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about your duty suspended spirits"))

  def postDutySuspendedSpirits: HttpRequestBuilder =
    http("Post Duty Suspended Spirits")
      .post(s"$baseUrl/$route/tell-us-about-your-spirits-in-duty-suspense")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("totalSpirits", 206.45)
      .formParam("pureAlcoholInSpirits", 20.55)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/tell-us-about-your-other-fermented-in-duty-suspense": String))

  def getDutySuspendedOtherFermentedProducts: HttpRequestBuilder =
    http("Get Duty Suspended Other Fermented Products")
      .get(s"$baseUrl/$route/tell-us-about-your-other-fermented-in-duty-suspense": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about your other fermented products with duty suspended"))

  def postDutySuspendedOtherFermentedProducts: HttpRequestBuilder =
    http("Post Duty Suspended Other Fermented Products")
      .post(s"$baseUrl/$route/tell-us-about-your-other-fermented-in-duty-suspense")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("totalOtherFermented", 306.45)
      .formParam("pureAlcoholInOtherFermented", 30.55)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/check-your-duty-suspended-deliveries": String))

  def getCheckYourAnswersDutySuspendedDeliveries: HttpRequestBuilder =
    http("Get Check Your Answers Duty Suspended Deliveries")
      .get(s"$baseUrl/$route/check-your-duty-suspended-deliveries": String)
      .check(status.is(200))
      .check(regex("Check your answers"))

}
