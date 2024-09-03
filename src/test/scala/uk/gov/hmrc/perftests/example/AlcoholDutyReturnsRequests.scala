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

  val baseUrl: String                = baseUrlFor("alcohol-duty-returns-frontend")
  val route: String                  = "manage-alcohol-duty"
  val CsrfPattern                    = """<input type="hidden" name="csrfToken" value="([^"]+)""""
  val authUrl: String                = baseUrlFor("auth-login-stub")
  val Year: Int                      = LocalDate.now().getYear
  val Month: Int                     = LocalDate.now().getMonthValue
  def periodKey(): String            =
    s"""${generateYear(Year: Int).toString.takeRight(2)}A${(generateMonth(Month: Int) + 64).toChar}"""
  def generateYear(Year: Int): Int   =
    if (generateMonth(Month: Int) == 12)
      Year - 1
    else
      Year
  def generateMonth(Month: Int): Int =
    if ((Month - 1) == 3 || (Month - 1) == 4 || (Month - 1) == 5)
      3
    else if ((Month - 1) == 6 || (Month - 1) == 7 || (Month - 1) == 8)
      6
    else if ((Month - 1) == 9 || (Month - 1) == 10 || (Month - 1) == 11)
      9
    else
      12

  def saveCsrfToken(): CheckBuilder[RegexCheckType, String, String] = regex(_ => CsrfPattern).saveAs("csrfToken")

  def getClearData: HttpRequestBuilder =
    http("Clear Data")
      .get(s"$baseUrl/$route/test-only/clear-all": String)
      .check(status.is(200))

  def getAuthLoginPage: HttpRequestBuilder =
    http("Navigate to auth login stub page")
      .get(s"$authUrl/auth-login-stub/gg-sign-in": String)
      .check(status.is(200))
      .check(regex("Authority Wizard").exists)
      .check(regex("CredID").exists)

  def postAuthLoginPage: HttpRequestBuilder =
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

  def getDeclareAlcoholDutyQuestion: HttpRequestBuilder =
    http("Navigate to Declaring Your Alcohol Duty Page")
      .get(s"$baseUrl/$route/do-you-need-to-declare-duty": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Declaring your alcohol duty"))

  def postDeclareAlcoholDutyQuestion: HttpRequestBuilder =
    http("Post Declaring Your Alcohol Duty Page")
      .post(s"$baseUrl/$route/do-you-need-to-declare-duty")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declareAlcoholDutyQuestion-yesNoValue", "true")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/which-types-do-you-need-to-declare": String))

  def getAlcoholTypesToDeclare: HttpRequestBuilder =
    http("Navigate to Alcohol Type to Declare Page")
      .get(s"$baseUrl/$route/which-types-do-you-need-to-declare": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What alcohol do you need to declare on this return?"))

  def postAlcoholTypesToDeclare: HttpRequestBuilder =
    http("Post Alcohol Type to Declare Page")
      .post(s"$baseUrl/$route/which-types-do-you-need-to-declare")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value[0]", "Beer")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/task-list/your-alcohol-duty-return": String))

  def getWhatDoYouNeedToDeclareBeerPage: HttpRequestBuilder =
    http("Navigate to What Do You Need To Declare Beer Page")
      .get(s"$baseUrl/$route/what-do-you-need-to-declare/Beer": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What do you need to declare?"))

  def postWhatDoYouNeedToDeclareBeerPage: HttpRequestBuilder =
    http("Post What Do You Need To Declare Beer Page")
      .post(s"$baseUrl/$route/what-do-you-need-to-declare/Beer")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("rateBand[311]", "311")
      .formParam("rateBand[321]", "321")
      .formParam("rateBand[331]", "331")
      .formParam("rateBand[341]", "341")
      .formParam("rateBand[351]", "351")
      .formParam("rateBand[356]", "356")
      .formParam("rateBand[361]", "361")
      .formParam("rateBand[366]", "366")
      .formParam("rateBand[371]", "371")
      .formParam("rateBand[376]", "376")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/how-much-do-you-need-to-declare/Beer": String))

  def getHowMuchYouNeedToDeclareBeerPage: HttpRequestBuilder =
    http("Navigate to How Much You Need To Declare Beer Page")
      .get(s"$baseUrl/$route/how-much-do-you-need-to-declare/Beer": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the beer you need to declare"))

  def postHowMuchYouNeedToDeclareBeerPage: HttpRequestBuilder =
    http("Post How Much You Need To Declare Beer Page")
      .post(s"$baseUrl/$route/how-much-do-you-need-to-declare/Beer")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumes[0].totalLitres", 945.55)
      .formParam("volumes[0].pureAlcohol", 55.5555)
      .formParam("volumes[1].totalLitres", 898.34)
      .formParam("volumes[1].pureAlcohol", 77.55)
      .formParam("volumes[2].totalLitres", 667.32)
      .formParam("volumes[2].pureAlcohol", 66.34)
      .formParam("volumes[3].totalLitres", 999.19)
      .formParam("volumes[3].pureAlcohol", 99.13)
      .formParam("volumes[4].totalLitres", 887.54)
      .formParam("volumes[4].pureAlcohol", 66.44)
      .formParam("volumes[5].totalLitres", 699.45)
      .formParam("volumes[5].pureAlcohol", 66.89)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/do-you-have-multiple-small-producer-relief-duty-rates/Beer": String))

  def getDoYouHaveMultipleSprDutyRateBeerPage: HttpRequestBuilder =
    http("Navigate to Do You Have Multiple Small Producer Relief Duty Rate Beer Page")
      .get(s"$baseUrl/$route/do-you-have-multiple-small-producer-relief-duty-rates/Beer": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("If you have more than one Small Producer Relief duty rate"))

  def postDoYouHaveMultipleSprDutyRateBeerPage: HttpRequestBuilder =
    http("Post Do You Have Multiple Small Producer Relief Duty Rate Beer Page")
      .post(s"$baseUrl/$route/do-you-have-multiple-small-producer-relief-duty-rates/Beer")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("doYouHaveMultipleSPRDutyRates-yesNoValue-no", "true")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/tell-us-about-single-spr-rate/Beer": String))

  def getSingleSprRateBeerPage: HttpRequestBuilder =
    http("Navigate to Single Small Producer Relief Rate Beer Page")
      .get(s"$baseUrl/$route/tell-us-about-single-spr-rate/Beer": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the beer that is eligible for Small Producer Relief"))

  def postSingleSprRateBeerPage: HttpRequestBuilder =
    http("Post How Much You Need To Declare Beer Page")
      .post(s"$baseUrl/$route/tell-us-about-single-spr-rate/Beer")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumesWithRate_0_totalLitres", "888.88")
      .formParam("volumesWithRate_0_pureAlcohol", "99.45")
      .formParam("volumesWithRate_0_dutyRate", "15")
      .formParam("volumesWithRate_1_totalLitres", "776.45")
      .formParam("volumesWithRate_1_pureAlcohol", "78.9")
      .formParam("volumesWithRate_1_dutyRate", "18")
      .formParam("volumesWithRate_2_totalLitres", "776.89")
      .formParam("volumesWithRate_2_pureAlcohol", "99.99")
      .formParam("volumesWithRate_2_dutyRate", "15")
      .formParam("volumesWithRate_3_totalLitres", "889.65")
      .formParam("volumesWithRate_3_pureAlcohol", "66.54")
      .formParam("volumesWithRate_3_dutyRate", "20")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/return-check-your-answers/Beer": String))

  def getCheckYourAnswersReturnsBeerPage: HttpRequestBuilder =
    http("Navigate to Check Your Answers Returns Beer Page")
      .get(s"$baseUrl/$route/return-check-your-answers/Beer": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Check your answers"))

  def getDutyDueBeerPage: HttpRequestBuilder =
    http("Navigate to Duty Due Page")
      .get(s"$baseUrl/$route/duty-due/Beer": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("You will owe £14,749.75"))

  def postDutyDueBeerPage: HttpRequestBuilder =
    http("Post Duty Due Page")
      .post(s"$baseUrl/$route/duty-due/Beer")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/task-list/your-alcohol-duty-return": String))
}
