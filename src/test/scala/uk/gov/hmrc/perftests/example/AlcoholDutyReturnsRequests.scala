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
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration
import io.gatling.core.check.CheckBuilder
import io.gatling.core.check.regex.RegexCheckType

object AlcoholDutyReturnsRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("alcohol-duty-returns-frontend")
  val route: String   = "manage-alcohol-duty"
  val CsrfPattern     = """<input type="hidden" name="csrfToken" value="([^"]+)""""
  val authUrl: String = baseUrlFor("auth-login-stub")

  def saveCsrfToken(): CheckBuilder[RegexCheckType, String, String] = regex(_ => CsrfPattern).saveAs("csrfToken")

  def getAuthLoginPage: HttpRequestBuilder =
    http("Navigate to auth login stub page")
      .get(s"$authUrl/auth-login-stub/gg-sign-in": String)
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
      .formParam("enrolment[0].name", "HMRC-AD-ORG")
      .formParam("enrolment[0].taxIdentifier[0].name", "APPAID")
      .formParam("enrolment[0].taxIdentifier[0].value", "XMADP0000000208")
      .formParam("redirectionUrl", s"$baseUrl/$route/before-you-start-your-return/24AC")
      .check(status.is(303))
      .check(header("Location").is(s"$baseUrl/$route/before-you-start-your-return/24AC": String))

  def postClearData: HttpRequestBuilder =
    http("Clear Data")
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
      .formParam("redirectionUrl", s"$baseUrl/$route/test-only/clear-all")
      .check(status.is(303))
  def getDeclareAlcoholDutyQuestion: HttpRequestBuilder =
    http("Navigate to Declare Alcohol Duty Question Page")
      .get(s"$baseUrl/$route/declareAlcoholDutyQuestion": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Do you need to declare duty on any alcoholic products?"))

  def postDeclareAlcoholDutyQuestion: HttpRequestBuilder =
    http("Post Declare Alcohol Duty Question")
      .post(s"$baseUrl/$route/declareAlcoholDutyQuestion")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declareAlcoholDutyQuestion-yesNoValue", "true")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/productEntryGuidance": String))

  def getProductEntryGuidancePage: HttpRequestBuilder =
    http("Navigate to Product Entry Guidance Page")
      .get(s"$baseUrl/$route/productEntryGuidance": String)
      .check(status.is(200))
      .check(regex("Tell us about your alcohol"))

  def navigateToProductNamePage: HttpRequestBuilder =
    http("Navigate to Product Name Page")
      .get(s"$baseUrl/$route/productName": String)
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
//      .check(regex("What is this product’s Alcohol by Volume (ABV) strength?"))

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

  def postDraughtReliefQuestion(trueOrFalse: Boolean): HttpRequestBuilder =
    http("Post Draught Relief Question")
      .post(s"$baseUrl/$route/draughtReliefQuestion")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("draught-relief-input", trueOrFalse)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/smallProducerReliefQuestion": String))

  def getSmallProducerReliefQuestion: HttpRequestBuilder =
    http("Get Small Producer Relief Question Page")
      .get(s"$baseUrl/$route/smallProducerReliefQuestion": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Is this product eligible for Small Producer Relief?"))

  def postSmallProducerReliefQuestion(trueOrFalse: Boolean): HttpRequestBuilder =
    http("Post Draught Relief Question")
      .post(s"$baseUrl/$route/smallProducerReliefQuestion")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("small-producer-relief-input", trueOrFalse)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/taxType": String))

  def getTaxTypeCode: HttpRequestBuilder =
    http("Get TaxType Code Page")
      .get(s"$baseUrl/$route/taxType": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Your product’s tax type code"))

  def postTaxTypeCode(idTaxTypeCode: String, smallProducerRelief: Boolean = true): HttpRequestBuilder = {
    val idTaxTypeCodeID = idTaxTypeCode match {
      case "Beer, tax type code 321" => "321_Beer"
      case "Wine, tax type code 378" => "378_Wine"
      case _                         => throw new IllegalArgumentException("Tax Type Code not found")
    }
    http("Post TaxType Code")
      .post(s"$baseUrl/$route/taxType": String)
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", idTaxTypeCodeID)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (smallProducerRelief) "declareSmallProducerReliefDutyRate"
        else "productVolume"}": String))
  }

  def getDeclareSmallProducerReliefDutyRate: HttpRequestBuilder =
    http("Get Declare Small Producer Relief DutyRate Page")
      .get(s"$baseUrl/$route/declareSmallProducerReliefDutyRate": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What is your Small Producer Relief duty rate?"))

  def postDeclareSmallProducerReliefDutyRate: HttpRequestBuilder =
    http("Post Declare Small Producer Relief DutyRate")
      .post(s"$baseUrl/$route/declareSmallProducerReliefDutyRate")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declareSmallProducerReliefDutyRate-input", 10.11)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/productVolume": String))

  def getProductVolumePage: HttpRequestBuilder =
    http("Get Product Volume Page")
      .get(s"$baseUrl/$route/productVolume": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much of this product do you need to declare?"))

  def postProductVolumePage: HttpRequestBuilder =
    http("Post Product Volume")
      .post(s"$baseUrl/$route/productVolume")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("product-volume-input", 32.87)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/you-have-declared-this-many-litres-of-pure-alcohol": String))

  def getPureAlcoholPage: HttpRequestBuilder =
    http("Get Pure Alcohol Page")
      .get(s"$baseUrl/$route/you-have-declared-this-many-litres-of-pure-alcohol": String)
      .check(status.is(200))
      .check(regex("This product has 1.643 litres of pure alcohol"))

  def getProductDutyRatePage(dutyRate: String): HttpRequestBuilder =
    http("Get Product Duty Rate Page")
      .get(s"$baseUrl/$route/the-duty-due-on-this-product": String)
      .check(status.is(200))
      .check(regex(s"""The duty due on this product is $dutyRate"""))
}
