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

object ViewPastPaymentsRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("alcohol-duty-returns-frontend")
  val route: String = "manage-alcohol-duty"
  val CsrfPattern = """<input type="hidden" name="csrfToken" value="([^"]+)""""
  val authUrl: String = baseUrlFor("auth-login-stub")

  def saveCsrfToken(): CheckBuilder[RegexCheckType, String, String] = regex(_ => CsrfPattern).saveAs("csrfToken")

  def postAuthLoginPageForViewPastPayments: HttpRequestBuilder =
    http("Login with user credentials")
      .post(s"$authUrl/auth-login-stub/gg-sign-in")
      .formParam("credentialStrength", "strong")
      .formParam("confidenceLevel", "50")
      .formParam("authorityId", "")
      .formParam("groupIdentifier", "")
      .formParam("email", "user@test.com")
      .formParam("credentialRole", "User")
      .formParam("affinityGroup", "Organisation")
      .formParam("enrolment[0].state", "Activated")
      .formParam("enrolment[0].name", "HMRC-AD-ORG")
      .formParam("enrolment[0].taxIdentifier[0].name", "APPAID")
      .formParam("enrolment[0].taxIdentifier[0].value", "XMADP0002100211")
      .formParam("redirectionUrl", s"$baseUrl/$route/view-payments")
      .check(status.is(303))
      .check(header("Location").is(s"$baseUrl/$route/view-payments": String))

  def getViewPastPaymentsPage: HttpRequestBuilder =
    http("Navigate to Alcohol Duty payments")
      .get(s"$baseUrl/$route/view-payments": String)
      .check(status.is(200))
      .check(regex("Alcohol Duty payments"))
}