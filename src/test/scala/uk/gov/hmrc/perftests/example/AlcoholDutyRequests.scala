/*
 * Copyright 2023 HM Revenue & Customs
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
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import io.gatling.core.check.CheckBuilder
import io.gatling.core.check.regex.RegexCheckType

object AlcoholDutyRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("alcohol-duty-returns-frontend")
  val route: String   = "manage-alcohol-duty"
  val CsrfPattern     = """<input type="hidden" name="csrfToken" value="([^"]+)""""
  val authUrl: String = baseUrlFor("auth-login-stub")

  def saveCsrfToken(): CheckBuilder[RegexCheckType, String, String] = regex(_ => CsrfPattern).saveAs("csrfToken")

  def getAuthLoginPage: HttpRequestBuilder =
    http("Navigate to auth login stub page")
      .get(s"$authUrl/auth-login-stub/gg-sign-in")
      .check(status.is(200))
      .check(regex("Authority Wizard").exists)
      .check(regex("CredID").exists)

  def loginWithAuthLoginStub: HttpRequestBuilder =
    http("Login with user credentials")
      .post(s"$authUrl/auth-login-stub/gg-sign-in")
      .formParam("credentialStrength", "strong")
      .formParam("confidenceLevel", "50")
      .formParam("authorityId", "")
      .formParam("groupIdentifier", "")
      .formParam("email", "user@test.com")
      .formParam("credentialRole", "User")
      .formParam("affinityGroup", "Individual")
      .formParam("enrolment[0].state", "Activated")
      .formParam("enrolment[0].name", "")
      .formParam("enrolment[0].taxIdentifier[0].name", "")
      .formParam("enrolment[0].taxIdentifier[0].value", "")
      .formParam("redirectionUrl", s"$baseUrl/$route/productName")
      .check(status.is(303))
      .check(header("Location").is(s"$baseUrl/$route/productName": String))

  def navigateToProductNamePage: HttpRequestBuilder =
    http("Navigate to product Name Page")
      .get(s"$baseUrl/$route/productName")
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What name do you want to give this product?"))

  def postProductName: HttpRequestBuilder =
    http("Post Product Name")
      .post(s"$baseUrl/$route/productName")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("product-name-input", "test")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/alcoholByVolumeQuestion": String))

  def getAlcoholByVolumeQuestion: HttpRequestBuilder =
    http("Get Alcohol By Volume Question Page")
      .get(s"$baseUrl/$route/alcoholByVolumeQuestion": String)
      .check(status.is(200))
      .check(saveCsrfToken())

  def postAlcoholByVolume: HttpRequestBuilder =
    http("Post Alcohol By Volume")
      .post(s"$baseUrl/$route/alcoholByVolumeQuestion")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("alcohol-by-volume-input", 5)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/draughtReliefQuestion": String))

  def getDraughtReliefQuestion: HttpRequestBuilder =
    http("Get Draught Relief Question Page")
      .get(s"$baseUrl/$route/draughtReliefQuestion": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Is this product eligible for Draught Relief?"))

  def postDraughtReliefQuestion: HttpRequestBuilder =
    http("Post Draught Relief Question")
      .post(s"$baseUrl/$route/draughtReliefQuestion")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("draught-relief-input", "true")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/smallProducerReliefQuestion": String))

  def getSmallProducerReliefQuestion: HttpRequestBuilder =
    http("Get Small Producer Relief Question Page")
      .get(s"$baseUrl/$route/smallProducerReliefQuestion": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Is this product eligible for Small Producer Relief?"))

  def postSmallProducerReliefQuestion: HttpRequestBuilder =
    http("Post Draught Relief Question")
      .post(s"$baseUrl/$route/smallProducerReliefQuestion")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("small-producer-relief-input", "true")
      .check(status.is(303))
}
