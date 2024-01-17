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

object DeclareDutySuspendedDeliveriesRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("alcohol-duty-returns-frontend")
  val route: String   = "manage-alcohol-duty"
  val CsrfPattern     = """<input type="hidden" name="csrfToken" value="([^"]+)""""

  def saveCsrfToken(): CheckBuilder[RegexCheckType, String, String] = regex(_ => CsrfPattern).saveAs("csrfToken")

  def getDeclareDutySuspendedDeliveriesQuestion: HttpRequestBuilder =
    http("Get Declare Duty Suspended Deliveries Question Page")
      .get(s"$baseUrl/$route/declareDutySuspendedDeliveriesQuestion": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Do you need to declare alcohol you delivered or received duty suspended?"))

  def postDeclareDutySuspendedDeliveriesQuestion: HttpRequestBuilder =
    http("Post Declare Duty Suspended Deliveries Question")
      .post(s"$baseUrl/$route/declareDutySuspendedDeliveriesQuestion")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declare-duty-suspended-deliveries-input", "true")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/dutySuspendedDeliveriesGuidance": String))

  def getDutySuspendedDeliveriesGuidance: HttpRequestBuilder =
    http("Get Duty Suspended Deliveries Guidance")
      .get(s"$baseUrl/$route/dutySuspendedDeliveriesGuidance": String)
      .check(status.is(200))
      .check(regex("Tell us about alcohol you delivered or received duty suspended"))

  def postDutySuspendedDeliveriesGuidance: HttpRequestBuilder =
    http("Test2 Post Duty Suspended Deliveries Guidance")
      .post(s"$baseUrl/$route/dutySuspendedDeliveriesGuidance")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(200))
      .check(header("Location").is(s"/$route/declareDutySuspendedDeliveriesOutsideUk": String))

  def getDeclareDutySuspendedDeliveriesOutsideUk: HttpRequestBuilder =
    http("Get Declare Duty Suspended Deliveries Outside UK")
      .get(s"$baseUrl/$route/declareDutySuspendedDeliveriesOutsideUk": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much have you delivered duty suspended outside of the UK?"))

  def postDeclareDutySuspendedDeliveriesOutsideUk: HttpRequestBuilder =
    http("Post Declare Duty Suspended Deliveries Outside UK")
      .post(s"$baseUrl/$route/declareDutySuspendedDeliveriesOutsideUk")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declare-duty-suspended-deliveries-outside-uk-input", 77)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/dutySuspendedDeliveries": String))

  def getDeclareDutySuspendedDeliveriesInsideUk: HttpRequestBuilder =
    http("Get Declare Duty Suspended Deliveries Inside UK")
      .get(s"$baseUrl/$route/dutySuspendedDeliveries": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much have you delivered duty suspended within the UK?"))

  def postDeclareDutySuspendedDeliveriesInsideUk: HttpRequestBuilder =
    http("Post Declare Duty Suspended Deliveries Inside UK")
      .post(s"$baseUrl/$route/dutySuspendedDeliveries")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("duty-suspended-deliveries-input", 88)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/declareDutySuspendedReceived": String))

  def getDeclareDutySuspendedReceived: HttpRequestBuilder =
    http("Get Declare Duty Suspended Received")
      .get(s"$baseUrl/$route/declareDutySuspendedReceived": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much have you received duty suspended?"))

  def postDeclareDutySuspendedReceived: HttpRequestBuilder =
    http("Post Declare Duty Suspended Received")
      .post(s"$baseUrl/$route/declareDutySuspendedReceived")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declare-duty-suspended-received-input", 99)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/check-your-answers-duty-suspended-deliveries": String))

  def getCheckYourAnswersDutySuspendedDeliveries: HttpRequestBuilder =
    http("Get Check Your Answers Duty Suspended Deliveries")
      .get(s"$baseUrl/$route/check-your-answers-duty-suspended-deliveries": String)
      .check(status.is(200))
      .check(regex("Check your answers"))

  def postCheckYourAnswersDutySuspendedDeliveries: HttpRequestBuilder =
    http("Post Check Your Answers Duty Suspended Deliveries")
      .post(s"$baseUrl/$route/check-your-answers-duty-suspended-deliveries")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/summary": String))
}
