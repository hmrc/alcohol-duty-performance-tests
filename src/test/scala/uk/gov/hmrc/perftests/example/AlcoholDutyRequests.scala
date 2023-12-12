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

object AlcoholDutyRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("alcohol-duty-returns-frontend")
  val route: String   = "manage-alcohol-duty"
  val authUrl: String     = baseUrlFor("auth-login-stub")

  def getAuthLoginPage: HttpRequestBuilder =
    http("Navigate to auth login stub page")
      .get(s"$authUrl/auth-login-stub/gg-sign-in")
      .check(status.is(200))
      .check(regex("Authority Wizard").exists)
      .check(regex("CredID").exists)

  def loginWithAuthLoginStub(): HttpRequestBuilder = {

    http("Login with user credentials")
      .post(s"$authUrl/auth-login-stub/gg-sign-in")
      .formParam("authorityId", "")
      .formParam("groupIdentifier", "")
      .formParam("credentialStrength", "Strong")
      .formParam("confidenceLevel", "50")
      .formParam("affinityGroup", "Individual")
      .formParam("enrolment[0].state", "Activated")
      .formParam("enrolment[0].name", "")
      .formParam("enrolment[0].taxIdentifier[0].name", "")
      .formParam("enrolment[0].taxIdentifier[0].value", "")
      .formParam("redirectionUrl", s"$baseUrl")
      .check(status.is(303))
      .check(header("Location").is(s"$baseUrl": String))
  }

  val navigateToProductNamePage: HttpRequestBuilder =
    http("Navigate to product Name Page")
      .get(s"$baseUrl/productName")
      .check(status.is(200))
      .check(css("input[name=csrfToken]", "value").saveAs("csrfToken"))

  val postProductName: HttpRequestBuilder =
    http("Post Product Name")
      .post(s"$baseUrl/productName": String)
      .formParam("productName", s"$${productName}")
      .formParam("csrfToken", s"$${csrfToken}")
      .check(status.is(303))
      //.check(header("Location").is("/check-your-vat-flat-rate/turnover").saveAs("turnOverPage"))

  val getAlcoholbyVolumeQuestion: HttpRequestBuilder =
    http("Get Alcohol By Volume Question Page")
      .get(s"$baseUrl/alcoholByVolumeQuestion": String)
      .check(status.is(200))
}
