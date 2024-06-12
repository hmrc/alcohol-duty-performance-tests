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

import java.time.LocalDate

object AlcoholDutyReturnsRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("alcohol-duty-returns-frontend")
  val route: String   = "manage-alcohol-duty"
  val CsrfPattern     = """<input type="hidden" name="csrfToken" value="([^"]+)""""
  val authUrl: String = baseUrlFor("auth-login-stub")
  val Year: Int = LocalDate.now().getYear()
  val Month: Int = LocalDate.now().getMonthValue()
  def periodKey(): String = s"""${generateYear(Year: Int).toString.takeRight(2)}A${(generateMonth(Month: Int) + 64).toChar}"""
  def generateYear(Year: Int): Int = {
    if (generateMonth(Month: Int) == 12)
      Year - 1
    else
      Year
  }
  def generateMonth(Month: Int): Int = {
    if ((Month - 1) == 3 || (Month - 1) == 4 || (Month - 1) == 5)
      3
    else if ((Month - 1) == 6 || (Month - 1) == 7 || (Month - 1) == 8)
      6
    else if ((Month - 1) == 9 || (Month - 1) == 10 || (Month - 1) == 11)
      9
    else
      12
  }

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
      .formParam("affinityGroup", "Organisation")
      .formParam("enrolment[0].state", "Activated")
      .formParam("enrolment[0].name", "HMRC-AD-ORG")
      .formParam("enrolment[0].taxIdentifier[0].name", "APPAID")
      .formParam("enrolment[0].taxIdentifier[0].value", "XMADP0000100208")
      .formParam("redirectionUrl", s"$baseUrl/$route/before-you-start-your-return/" + periodKey())
      .check(status.is(303))
      .check(header("Location").is(s"$baseUrl/$route/before-you-start-your-return/" + periodKey(): String))

  def getClearData: HttpRequestBuilder =
    http("Clear Data")
      .get(s"$baseUrl/$route/test-only/clear-all": String)
      .check(status.is(200))

  def getDeclareAlcoholDutyQuestion: HttpRequestBuilder =
    http("Navigate to Declare Alcohol Duty Question Page")
      .get(s"$baseUrl/$route/do-you-need-to-declare-duty": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Do you need to declare duty on any alcoholic products?"))

  def postDeclareAlcoholDutyQuestion: HttpRequestBuilder =
    http("Post Declare Alcohol Duty Question")
      .post(s"$baseUrl/$route/do-you-need-to-declare-duty")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declareAlcoholDutyQuestion-yesNoValue", "true")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/tell-us-about-your-alcohol": String))

  def getProductEntryGuidancePage: HttpRequestBuilder =
    http("Navigate to Product Entry Guidance Page")
      .get(s"$baseUrl/$route/tell-us-about-your-alcohol": String)
      .check(status.is(200))
      .check(regex("Tell us about your alcohol"))

  def navigateToProductNamePage: HttpRequestBuilder =
    http("Navigate to Product Name Page")
      .get(s"$baseUrl/$route/what-name-do-you-want-to-give-this-product": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What name do you want to give this product?"))

  def postProductName: HttpRequestBuilder =
    http("Post Product Name")
      .post(s"$baseUrl/$route/what-name-do-you-want-to-give-this-product")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("product-name-input", "test")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/what-is-this-products-alcohol-by-volume-strength": String))

  def getAlcoholByVolumeQuestion: HttpRequestBuilder =
    http("Get Alcohol By Volume Question Page")
      .get(s"$baseUrl/$route/what-is-this-products-alcohol-by-volume-strength": String)
      .check(status.is(200))
      .check(saveCsrfToken())
//      .check(regex("What is this product’s Alcohol by Volume (ABV) strength?"))

  def postAlcoholByVolume: HttpRequestBuilder =
    http("Post Alcohol By Volume")
      .post(s"$baseUrl/$route/what-is-this-products-alcohol-by-volume-strength")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("alcohol-by-volume-input", 5)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/is-this-product-eligible-for-draught-relief": String))

  def getDraughtReliefQuestion: HttpRequestBuilder =
    http("Get Draught Relief Question Page")
      .get(s"$baseUrl/$route/is-this-product-eligible-for-draught-relief": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Is this product eligible for Draught Relief?"))

  def postDraughtReliefQuestion(trueOrFalse: Boolean): HttpRequestBuilder =
    http("Post Draught Relief Question")
      .post(s"$baseUrl/$route/is-this-product-eligible-for-draught-relief")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("draught-relief-input", trueOrFalse)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/is-this-product-eligible-for-small-producer-relief": String))

  def getSmallProducerReliefQuestion: HttpRequestBuilder =
    http("Get Small Producer Relief Question Page")
      .get(s"$baseUrl/$route/is-this-product-eligible-for-small-producer-relief": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Is this product eligible for Small Producer Relief?"))

  def postSmallProducerReliefQuestion(trueOrFalse: Boolean): HttpRequestBuilder =
    http("Post Draught Relief Question")
      .post(s"$baseUrl/$route/is-this-product-eligible-for-small-producer-relief")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("small-producer-relief-input", trueOrFalse)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/what-is-this-products-tax-type": String))

  def getTaxTypeCode: HttpRequestBuilder =
    http("Get TaxType Code Page")
      .get(s"$baseUrl/$route/what-is-this-products-tax-type": String)
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
      .post(s"$baseUrl/$route/what-is-this-products-tax-type": String)
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value", idTaxTypeCodeID)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (smallProducerRelief) "what-is-your-small-producer-duty-rate"
        else "how-much-of-this-product-do-you-need-to-declare"}": String))
  }

  def getDeclareSmallProducerReliefDutyRate: HttpRequestBuilder =
    http("Get Declare Small Producer Relief DutyRate Page")
      .get(s"$baseUrl/$route/what-is-your-small-producer-duty-rate": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What is your Small Producer Relief duty rate?"))

  def postDeclareSmallProducerReliefDutyRate: HttpRequestBuilder =
    http("Post Declare Small Producer Relief DutyRate")
      .post(s"$baseUrl/$route/what-is-your-small-producer-duty-rate")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declareSmallProducerReliefDutyRate-input", 10.11)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/how-much-of-this-product-do-you-need-to-declare": String))

  def getProductVolumePage: HttpRequestBuilder =
    http("Get Product Volume Page")
      .get(s"$baseUrl/$route/how-much-of-this-product-do-you-need-to-declare": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("How much of this product do you need to declare?"))

  def postProductVolumePage: HttpRequestBuilder =
    http("Post Product Volume")
      .post(s"$baseUrl/$route/how-much-of-this-product-do-you-need-to-declare")
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
