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
      .get(s"$baseUrl/$route/before-you-start-your-return/"  + periodKey: String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Before you start"))

  def postBeforeYouStartPage: HttpRequestBuilder =
    http("Post Before You Start Page")
      .post(s"$baseUrl/$route/before-you-start-your-return": String)
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/task-list": String))

  def getTaskListPage: HttpRequestBuilder =
    http("Get Task List Page")
      .get(s"$baseUrl/$route/complete-return/task-list": String)
      .check(status.is(200))
      .check(regex("Complete your Alcohol Duty Return"))

  def getDeclareDutySuspendedDeliveriesQuestion: HttpRequestBuilder =
    http("Get Declare Duty Suspended Deliveries Question Page")
      .get(s"$baseUrl/$route//complete-return/duty-suspended-deliveries/do-you-need-to-report": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Do you need to tell us about any duty suspended alcohol?"))

  def postDeclareDutySuspendedDeliveriesQuestion(declareDSDQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Declare Duty Suspended Deliveries Question")
      .post(s"$baseUrl/$route//complete-return/duty-suspended-deliveries/do-you-need-to-report")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declare-duty-suspended-deliveries-input", declareDSDQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (declareDSDQuestion) "complete-return/duty-suspended-deliveries/report/calculating-volumes"
        else "complete-return/task-list"}": String))

  def getDutySuspendedDeliveriesGuidance: HttpRequestBuilder =
    http("Get Duty Suspended Deliveries Guidance")
      .get(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/report/calculating-volumes": String)
      .check(status.is(200))
      .check(regex("Calculate your duty suspended alcohol"))

  def getDutySuspendedBeer: HttpRequestBuilder =
    http("Get Duty Suspended Beer")
      .get(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/Beer/report": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about your duty suspended beer"))

  def postDutySuspendedBeer: HttpRequestBuilder =
    http("Post Duty Suspended Beer")
      .post(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/Beer/report")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("totalBeer", 406.45)
      .formParam("pureAlcoholInBeer", 40.55)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/duty-suspended-deliveries/Cider/report": String))

  def getDutySuspendedCider: HttpRequestBuilder =
    http("Get Duty Suspended Cider")
      .get(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/Cider/report": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about your duty suspended cider"))

  def postDutySuspendedCider: HttpRequestBuilder =
    http("Post Duty Suspended Cider")
      .post(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/Cider/report")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("totalCider", 306.45)
      .formParam("pureAlcoholInCider", 30.55)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/duty-suspended-deliveries/Wine/report": String))

  def getDutySuspendedWine: HttpRequestBuilder =
    http("Get Duty Suspended Wine")
      .get(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/Wine/report": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about your duty suspended wine"))

  def postDutySuspendedWine: HttpRequestBuilder =
    http("Post Duty Suspended Wine")
      .post(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/Wine/report")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("totalWine", 506.45)
      .formParam("pureAlcoholInWine", 50.55)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/duty-suspended-deliveries/Spirits/report": String))

  def getDutySuspendedSpirits: HttpRequestBuilder =
    http("Get Duty Suspended Spirits")
      .get(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/Spirits/report": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about your duty suspended spirits"))

  def postDutySuspendedSpirits: HttpRequestBuilder =
    http("Post Duty Suspended Spirits")
      .post(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/Spirits/report")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("totalSpirits", 206.45)
      .formParam("pureAlcoholInSpirits", 20.55)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/duty-suspended-deliveries/OtherFermentedProducts/report": String))

  def getDutySuspendedOtherFermentedProducts: HttpRequestBuilder =
    http("Get Duty Suspended Other Fermented Products")
      .get(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/OtherFermentedProducts/report": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about your other fermented products with duty suspended"))

  def postDutySuspendedOtherFermentedProducts: HttpRequestBuilder =
    http("Post Duty Suspended Other Fermented Products")
      .post(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/OtherFermentedProducts/report")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("totalOtherFermented", 306.45)
      .formParam("pureAlcoholInOtherFermented", 30.55)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/duty-suspended-deliveries/check-your-answers": String))

  def getCheckYourAnswersDutySuspendedDeliveries: HttpRequestBuilder =
    http("Get Check Your Answers Duty Suspended Deliveries")
      .get(s"$baseUrl/$route/complete-return/duty-suspended-deliveries/check-your-answers": String)
      .check(status.is(200))
      .check(regex("Check your answers"))

}
