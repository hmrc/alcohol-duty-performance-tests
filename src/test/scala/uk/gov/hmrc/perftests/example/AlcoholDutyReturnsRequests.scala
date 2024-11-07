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
import scala.util.Random
object AlcoholDutyReturnsRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("alcohol-duty-returns-frontend")
  val route: String   = "manage-alcohol-duty"
  val CsrfPattern     = """<input type="hidden" name="csrfToken" value="([^"]+)""""
  val authUrl: String = baseUrlFor("auth-login-stub")
  val Year: Int       = LocalDate.now().getYear
  val Month: Int      = LocalDate.now().getMonthValue

  val periodKey: String =
    s"""${generateYear(Year: Int).toString.takeRight(2)}A${(generateMonth(Month: Int) + 64).toChar}"""

  def generateYear(Year: Int): Int =
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

  private val characters     = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
  private val random         = new Random()
  private val usedReferences = scala.collection.mutable.Set[String]()

  def generateUniqueReference(length: Int): String = {
    var reference: String = ""
    do reference = (1 to length).map(_ => characters(random.nextInt(characters.length))).mkString while (usedReferences
      .contains(reference))
    usedReferences.add(reference)
    reference
  }

  def saveCsrfToken(): CheckBuilder[RegexCheckType, String, String] = regex(_ => CsrfPattern).saveAs("csrfToken")

  def getClearData: HttpRequestBuilder =
    http("Clear Data")
      .get(s"$baseUrl/$route/test-only/clear-all": String)
      .check(status.is(200))

  def getClearReturnData: HttpRequestBuilder =
    http("Clear Data")
      .get(baseUrl + "/" + route + "/test-only/clear-return/${appaId}/24AI")
      .check(status.is(200))
      .check(regex("All return data is cleared").exists)

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
      .formParam("enrolment[0].taxIdentifier[0].value", "${appaId}")
      .formParam("redirectionUrl", s"$baseUrl/$route/before-you-start-your-return/" + periodKey )
      .check(status.is(303))
      .check(header("Location").is(s"$baseUrl/$route/before-you-start-your-return/" + periodKey: String))

  def getDeclareAlcoholDutyQuestion: HttpRequestBuilder =
    http("Navigate to Declaring Your Alcohol Duty Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/do-you-need-to-declare": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Declaring alcoholic products for duty"))

  def postDeclareAlcoholDutyQuestion(declareAlcoholDutyQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Declaring Your Alcohol Duty Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/do-you-need-to-declare")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("declareAlcoholDutyQuestion-yesNoValue", declareAlcoholDutyQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (declareAlcoholDutyQuestion) "complete-return/alcoholic-products/alcoholic-products-to-include"
        else "complete-return/task-list"}": String))

  def getAlcoholTypesToDeclare: HttpRequestBuilder =
    http("Navigate to Alcohol Type to Declare Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/alcoholic-products-to-include": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Which types of alcoholic products do you need to declare?"))

  def postAlcoholTypesToDeclare: HttpRequestBuilder =
    http("Post Alcohol Type to Declare Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/alcoholic-products-to-include")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("value[0]", "Beer")
      .formParam("value[1]", "Cider")
      .formParam("value[2]", "Wine")
      .formParam("value[3]", "Spirits")
      .formParam("value[4]", "OtherFermentedProduct")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/task-list": String))

  /*Beer pages*/
  def getWhatDoYouNeedToDeclareBeerPage: HttpRequestBuilder =
    http("Navigate to What Do You Need To Declare Beer Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/products-to-include": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What do you need to declare?"))

  def postWhatDoYouNeedToDeclareBeerPage: HttpRequestBuilder =
    http("Post What Do You Need To Declare Beer Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/products-to-include")
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
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Beer/declare/volumes": String))

  def getHowMuchYouNeedToDeclareBeerPage: HttpRequestBuilder =
    http("Navigate to How Much You Need To Declare Beer Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the beer you need to declare"))

  def postHowMuchYouNeedToDeclareBeerPage: HttpRequestBuilder =
    http("Post How Much You Need To Declare Beer Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumes[0].totalLitres", 945.55)
      .formParam("volumes[0].pureAlcohol", 55.5555)
      .formParam("volumes[0].taxType", 311)
      .formParam("volumes[1].totalLitres", 898.34)
      .formParam("volumes[1].pureAlcohol", 77.55)
      .formParam("volumes[1].taxType", 321)
      .formParam("volumes[2].totalLitres", 667.32)
      .formParam("volumes[2].pureAlcohol", 66.34)
      .formParam("volumes[2].taxType", 331)
      .formParam("volumes[3].totalLitres", 999.19)
      .formParam("volumes[3].pureAlcohol", 99.13)
      .formParam("volumes[3].taxType", 341)
      .formParam("volumes[4].totalLitres", 887.54)
      .formParam("volumes[4].pureAlcohol", 66.44)
      .formParam("volumes[4].taxType", 351)
      .formParam("volumes[5].totalLitres", 699.45)
      .formParam("volumes[5].pureAlcohol", 66.89)
      .formParam("volumes[5].taxType", 356)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Beer/declare/spr/multiple-duty-rates": String))

  def getDoYouHaveMultipleSprDutyRateBeerPage: HttpRequestBuilder =
    http("Navigate to Do You Have Multiple Small Producer Relief Duty Rate Beer Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/spr/multiple-duty-rates": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("If you have more than one Small Producer Relief duty rate"))

  def postDoYouHaveMultipleSprDutyRateBeerPage(multipleSprDutyRateQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Do You Have Multiple Small Producer Relief Duty Rate Beer Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/spr/multiple-duty-rates")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("doYouHaveMultipleSPRDutyRates-yesNoValue", multipleSprDutyRateQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (multipleSprDutyRateQuestion) "complete-return/alcoholic-products/Beer/declare/spr/multiple-duty-rates/eligible-volume"
        else "complete-return/alcoholic-products/Beer/declare/spr/eligible-volumes"}": String))

  def getSingleSprRateBeerPage: HttpRequestBuilder =
    http("Navigate to Single Small Producer Relief Rate Beer Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/spr/eligible-volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the beer that is eligible for Small Producer Relief"))

  def postSingleSprRateBeerPage: HttpRequestBuilder =
    http("Post Single Small Producer Relief Rate Beer Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/spr/eligible-volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumesWithRate[0].totalLitres", 888.88)
      .formParam("volumesWithRate[0].pureAlcohol", 99.45)
      .formParam("volumesWithRate[0].dutyRate", 15)
      .formParam("volumesWithRate[0].taxType", 361)
      .formParam("volumesWithRate[1].totalLitres", 776.45)
      .formParam("volumesWithRate[1].pureAlcohol", 78.9)
      .formParam("volumesWithRate[1].dutyRate", 18)
      .formParam("volumesWithRate[1].taxType", 366)
      .formParam("volumesWithRate[2].totalLitres", 776.89)
      .formParam("volumesWithRate[2].pureAlcohol", 99.99)
      .formParam("volumesWithRate[2].dutyRate", 15)
      .formParam("volumesWithRate[2].taxType", 361)
      .formParam("volumesWithRate[3].totalLitres", 889.65)
      .formParam("volumesWithRate[3].pureAlcohol", 66.54)
      .formParam("volumesWithRate[3].dutyRate", 20)
      .formParam("volumesWithRate[3].taxType", 376)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Beer/declare/check-your-answers": String))

  def getCheckYourAnswersReturnsBeerPage: HttpRequestBuilder =
    http("Navigate to Check Your Answers Returns Beer Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/check-your-answers": String)
      .check(status.is(200))
      .check(regex("Check your answers"))

  def getDutyDueBeerPage(dutyDueAmount: String): HttpRequestBuilder =
    http("Navigate to Duty Due Beer Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/duty-due": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("You will owe " + dutyDueAmount))

  def postDutyDueBeerPage: HttpRequestBuilder =
    http("Post Duty Due Beer Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/duty-due")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/task-list": String))

  /*Beer - SPR Yes pages*/
  def getMultipleSprRateBeerPage: HttpRequestBuilder =
    http("Navigate to Multiple Small Producer Relief Rate Beer Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/spr/multiple-duty-rates/eligible-volume": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the beer you need to declare that are eligible for Small Producer Relief"))

  def postMultipleSprRateBeerPage: HttpRequestBuilder =
    http("Post Multiple Small Producer Relief Rate Beer Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/spr/multiple-duty-rates/eligible-volume")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumesWithRate.taxType", "361")
      .formParam("volumesWithRate.totalLitres", 9999.99)
      .formParam("volumesWithRate.pureAlcohol", 89.9999)
      .formParam("volumesWithRate.dutyRate", 19)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Beer/declare/spr/check-your-answers": String))

  def getCheckYourAnswersSprBeerPage: HttpRequestBuilder =
    http("Navigate to Check Your Answers SPR Beer Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/spr/check-your-answers": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Check your answers"))

  def postCheckYourAnswersSprBeerPage: HttpRequestBuilder =
    http("Post Check Your Answers SPR Beer Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/spr/check-your-answers")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Beer/declare/spr/multiple-duty-rates/eligible-volumes": String))

  def getMultipleSprListQuestionBeerPage: HttpRequestBuilder =
    http("Navigate to Multiple SPR List Question Beer Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/spr/multiple-duty-rates/eligible-volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Beer with Small Producer Relief duty to declare"))

  def getRemoveProductPage(alcoholType: String): HttpRequestBuilder =
    http(s"Navigate to Remove $alcoholType Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/$alcoholType/declare/spr/multiple-duty-rates/remove-volume?index=0": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex {
        if (alcoholType == "OtherFermentedProduct") s"Remove this other fermented products entry?"
        else s"Remove this ${alcoholType.toLowerCase} entry?"
      })

  def postRemoveProductPage(removeProduct: Boolean = true, alcoholType: String): HttpRequestBuilder =
    http(s"Post Remove $alcoholType Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/$alcoholType/declare/spr/multiple-duty-rates/remove-volume?index=0")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("deleteMultipleSPREntry-yesNoValue", removeProduct)
      .check(status.is(303))
      .check(
        header("Location")
          .is(s"/$route/${
            if (removeProduct) s"complete-return/alcoholic-products/$alcoholType/declare/spr/multiple-duty-rates"
            else s"complete-return/alcoholic-products/$alcoholType/declare/spr/multiple-duty-rates/eligible-volumes"
          }": String)
      )

  def postMultipleSprListQuestionBeerPage(multipleSprListQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Multiple SPR List Question Beer Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Beer/declare/spr/multiple-duty-rates/eligible-volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("multipleSPRList-yesNoValue", multipleSprListQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (multipleSprListQuestion) "complete-return/alcoholic-products/Beer/change/spr/multiple-duty-rates/eligible-volume"
        else "complete-return/alcoholic-products/Beer/declare/check-your-answers"}": String))

  /*Cider pages*/
  def getWhatDoYouNeedToDeclareCiderPage: HttpRequestBuilder =
    http("Navigate to What Do You Need To Declare Cider Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/products-to-include": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What do you need to declare?"))

  def postWhatDoYouNeedToDeclareCiderPage: HttpRequestBuilder =
    http("Post What Do You Need To Declare Cider Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/products-to-include")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("rateBand[312]", "312")
      .formParam("rateBand[322]", "322")
      .formParam("rateBand[352]", "352")
      .formParam("rateBand[357]", "357")
      .formParam("rateBand[362]", "362")
      .formParam("rateBand[367]", "367")
      .formParam("rateBand[372]", "372")
      .formParam("rateBand[377]", "377")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Cider/declare/volumes": String))

  def getHowMuchYouNeedToDeclareCiderPage: HttpRequestBuilder =
    http("Navigate to How Much You Need To Declare Cider Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the cider you need to declare"))

  def postHowMuchYouNeedToDeclareCiderPage: HttpRequestBuilder =
    http("Post How Much You Need To Declare Cider Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumes[0].totalLitres", 945.55)
      .formParam("volumes[0].pureAlcohol", 55.5555)
      .formParam("volumes[0].taxType", 312)
      .formParam("volumes[1].totalLitres", 898.34)
      .formParam("volumes[1].pureAlcohol", 77.55)
      .formParam("volumes[1].taxType", 322)
      .formParam("volumes[2].totalLitres", 667.32)
      .formParam("volumes[2].pureAlcohol", 66.34)
      .formParam("volumes[2].taxType", 352)
      .formParam("volumes[3].totalLitres", 999.19)
      .formParam("volumes[3].pureAlcohol", 99.13)
      .formParam("volumes[3].taxType", 357)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Cider/declare/spr/multiple-duty-rates": String))

  def getDoYouHaveMultipleSprDutyRateCiderPage: HttpRequestBuilder =
    http("Navigate to Do You Have Multiple Small Producer Relief Duty Rate Cider Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/spr/multiple-duty-rates": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("If you have more than one Small Producer Relief duty rate"))

  def postDoYouHaveMultipleSprDutyRateCiderPage(multipleSprDutyRateQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Do You Have Multiple Small Producer Relief Duty Rate Cider Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/spr/multiple-duty-rates")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("doYouHaveMultipleSPRDutyRates-yesNoValue", multipleSprDutyRateQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (multipleSprDutyRateQuestion) "complete-return/alcoholic-products/Cider/declare/spr/multiple-duty-rates/eligible-volume"
        else "complete-return/alcoholic-products/Cider/declare/spr/eligible-volumes"}": String))

  def getSingleSprRateCiderPage: HttpRequestBuilder =
    http("Navigate to Single Small Producer Relief Rate Cider Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/spr/eligible-volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the cider that is eligible for Small Producer Relief"))

  def postSingleSprRateCiderPage: HttpRequestBuilder =
    http("Post Single Small Producer Relief Rate Cider Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/spr/eligible-volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumesWithRate[0].totalLitres", 888.88)
      .formParam("volumesWithRate[0].pureAlcohol", 99.45)
      .formParam("volumesWithRate[0].dutyRate", 15)
      .formParam("volumesWithRate[0].taxType", 362)
      .formParam("volumesWithRate[1].totalLitres", 776.45)
      .formParam("volumesWithRate[1].pureAlcohol", 78.9)
      .formParam("volumesWithRate[1].dutyRate", 18)
      .formParam("volumesWithRate[1].taxType", 367)
      .formParam("volumesWithRate[2].totalLitres", 776.89)
      .formParam("volumesWithRate[2].pureAlcohol", 99.99)
      .formParam("volumesWithRate[2].dutyRate", 15)
      .formParam("volumesWithRate[2].taxType", 372)
      .formParam("volumesWithRate[3].totalLitres", 889.65)
      .formParam("volumesWithRate[3].pureAlcohol", 66.54)
      .formParam("volumesWithRate[3].dutyRate", 20)
      .formParam("volumesWithRate[3].taxType", 377)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Cider/declare/check-your-answers": String))

  def getCheckYourAnswersReturnsCiderPage: HttpRequestBuilder =
    http("Navigate to Check Your Answers Returns Cider Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/check-your-answers": String)
      .check(status.is(200))
      .check(regex("Check your answers"))

  def getDutyDueCiderPage(dutyDueAmount: String): HttpRequestBuilder =
    http("Navigate to Duty Due Cider Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/duty-due": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("You will owe " + dutyDueAmount))

  def postDutyDueCiderPage: HttpRequestBuilder =
    http("Post Duty Due Cider Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/duty-due")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/task-list": String))

  /*Cider - SPR Yes pages*/
  def getMultipleSprRateCiderPage: HttpRequestBuilder =
    http("Navigate to Multiple Small Producer Relief Rate Cider Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/spr/multiple-duty-rates/eligible-volume": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the cider you need to declare that are eligible for Small Producer Relief"))

  def postMultipleSprRateCiderPage: HttpRequestBuilder =
    http("Post Multiple Small Producer Relief Rate Cider Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/spr/multiple-duty-rates/eligible-volume")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumesWithRate.taxType", "362")
      .formParam("volumesWithRate.totalLitres", 9999.99)
      .formParam("volumesWithRate.pureAlcohol", 89.9999)
      .formParam("volumesWithRate.dutyRate", 19)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Cider/declare/spr/check-your-answers": String))

  def getCheckYourAnswersSprCiderPage: HttpRequestBuilder =
    http("Navigate to Check Your Answers SPR Cider Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/spr/check-your-answers": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Check your answers"))

  def postCheckYourAnswersSprCiderPage: HttpRequestBuilder =
    http("Post Check Your Answers SPR Cider Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/spr/check-your-answers")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Cider/declare/spr/multiple-duty-rates/eligible-volumes": String))

  def getMultipleSprListQuestionCiderPage: HttpRequestBuilder =
    http("Navigate to Multiple SPR List Question Cider Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/spr/multiple-duty-rates/eligible-volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Cider with Small Producer Relief duty to declare"))

  def postMultipleSprListQuestionCiderPage(multipleSprListQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Multiple SPR List Question Cider Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Cider/declare/spr/multiple-duty-rates/eligible-volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("multipleSPRList-yesNoValue", multipleSprListQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (multipleSprListQuestion) "complete-return/alcoholic-products/Cider/change/spr/multiple-duty-rates/eligible-volume"
        else "complete-return/alcoholic-products/Cider/declare/check-your-answers"}": String))

  /*Wine pages*/
  def getWhatDoYouNeedToDeclareWinePage: HttpRequestBuilder =
    http("Navigate to What Do You Need To Declare Wine Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/products-to-include": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What do you need to declare?"))

  def postWhatDoYouNeedToDeclareWinePage: HttpRequestBuilder =
    http("Post What Do You Need To Declare Wine Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/products-to-include")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("rateBand[313]", "313")
      .formParam("rateBand[323]", "323")
      .formParam("rateBand[333]", "333")
      .formParam("rateBand[343]", "343")
      .formParam("rateBand[353]", "353")
      .formParam("rateBand[358]", "358")
      .formParam("rateBand[363]", "363")
      .formParam("rateBand[368]", "368")
      .formParam("rateBand[373]", "373")
      .formParam("rateBand[378]", "378")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Wine/declare/volumes": String))

  def getHowMuchYouNeedToDeclareWinePage: HttpRequestBuilder =
    http("Navigate to How Much You Need To Declare Wine Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the wine you need to declare"))

  def postHowMuchYouNeedToDeclareWinePage: HttpRequestBuilder =
    http("Post How Much You Need To Declare Wine Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumes[0].totalLitres", 945.55)
      .formParam("volumes[0].pureAlcohol", 55.5555)
      .formParam("volumes[0].taxType", 313)
      .formParam("volumes[1].totalLitres", 898.34)
      .formParam("volumes[1].pureAlcohol", 77.55)
      .formParam("volumes[1].taxType", 323)
      .formParam("volumes[2].totalLitres", 667.32)
      .formParam("volumes[2].pureAlcohol", 66.34)
      .formParam("volumes[2].taxType", 333)
      .formParam("volumes[3].totalLitres", 999.19)
      .formParam("volumes[3].pureAlcohol", 99.13)
      .formParam("volumes[3].taxType", 343)
      .formParam("volumes[4].totalLitres", 887.54)
      .formParam("volumes[4].pureAlcohol", 66.44)
      .formParam("volumes[4].taxType", 353)
      .formParam("volumes[5].totalLitres", 699.45)
      .formParam("volumes[5].pureAlcohol", 66.89)
      .formParam("volumes[5].taxType", 358)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Wine/declare/spr/multiple-duty-rates": String))

  def getDoYouHaveMultipleSprDutyRateWinePage: HttpRequestBuilder =
    http("Navigate to Do You Have Multiple Small Producer Relief Duty Rate Wine Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/spr/multiple-duty-rates": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("If you have more than one Small Producer Relief duty rate"))

  def postDoYouHaveMultipleSprDutyRateWinePage(multipleSprDutyRateQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Do You Have Multiple Small Producer Relief Duty Rate Wine Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/spr/multiple-duty-rates")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("doYouHaveMultipleSPRDutyRates-yesNoValue", multipleSprDutyRateQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (multipleSprDutyRateQuestion) "complete-return/alcoholic-products/Wine/declare/spr/multiple-duty-rates/eligible-volume"
        else "complete-return/alcoholic-products/Wine/declare/spr/eligible-volumes"}": String))

  def getSingleSprRateWinePage: HttpRequestBuilder =
    http("Navigate to Single Small Producer Relief Rate Wine Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/spr/eligible-volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the wine that is eligible for Small Producer Relief"))

  def postSingleSprRateWinePage: HttpRequestBuilder =
    http("Post Single Small Producer Relief Rate Wine Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/spr/eligible-volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumesWithRate[0].totalLitres", 888.88)
      .formParam("volumesWithRate[0].pureAlcohol", 99.45)
      .formParam("volumesWithRate[0].dutyRate", 15)
      .formParam("volumesWithRate[0].taxType", 363)
      .formParam("volumesWithRate[1].totalLitres", 776.45)
      .formParam("volumesWithRate[1].pureAlcohol", 78.9)
      .formParam("volumesWithRate[1].dutyRate", 18)
      .formParam("volumesWithRate[1].taxType", 368)
      .formParam("volumesWithRate[2].totalLitres", 776.89)
      .formParam("volumesWithRate[2].pureAlcohol", 99.99)
      .formParam("volumesWithRate[2].dutyRate", 15)
      .formParam("volumesWithRate[2].taxType", 373)
      .formParam("volumesWithRate[3].totalLitres", 889.65)
      .formParam("volumesWithRate[3].pureAlcohol", 66.54)
      .formParam("volumesWithRate[3].dutyRate", 20)
      .formParam("volumesWithRate[3].taxType", 378)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Wine/declare/check-your-answers": String))

  def getCheckYourAnswersReturnsWinePage: HttpRequestBuilder =
    http("Navigate to Check Your Answers Returns Wine Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/check-your-answers": String)
      .check(status.is(200))
      .check(regex("Check your answers"))

  def getDutyDueWinePage(dutyDueAmount: String): HttpRequestBuilder =
    http("Navigate to Duty Due Wine Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/duty-due": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("You will owe " + dutyDueAmount))

  def postDutyDueWinePage: HttpRequestBuilder =
    http("Post Duty Due Wine Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/duty-due")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/task-list": String))

  /*Wine - SPR Yes pages*/
  def getMultipleSprRateWinePage: HttpRequestBuilder =
    http("Navigate to Multiple Small Producer Relief Rate Wine Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/spr/multiple-duty-rates/eligible-volume": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the wine you need to declare that are eligible for Small Producer Relief"))

  def postMultipleSprRateWinePage: HttpRequestBuilder =
    http("Post Multiple Small Producer Relief Rate Wine Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/spr/multiple-duty-rates/eligible-volume")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumesWithRate.taxType", "363")
      .formParam("volumesWithRate.totalLitres", 9999.99)
      .formParam("volumesWithRate.pureAlcohol", 89.9999)
      .formParam("volumesWithRate.dutyRate", 19)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Wine/declare/spr/check-your-answers": String))

  def getCheckYourAnswersSprWinePage: HttpRequestBuilder =
    http("Navigate to Check Your Answers SPR Wine Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/spr/check-your-answers": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Check your answers"))

  def postCheckYourAnswersSprWinePage: HttpRequestBuilder =
    http("Post Check Your Answers SPR Wine Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/spr/check-your-answers")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Wine/declare/spr/multiple-duty-rates/eligible-volumes": String))

  def getMultipleSprListQuestionWinePage: HttpRequestBuilder =
    http("Navigate to Multiple SPR List Question Wine Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/spr/multiple-duty-rates/eligible-volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Wine with Small Producer Relief duty to declare"))

  def postMultipleSprListQuestionWinePage(multipleSprListQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Multiple SPR List Question Wine Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Wine/declare/spr/multiple-duty-rates/eligible-volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("multipleSPRList-yesNoValue", multipleSprListQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (multipleSprListQuestion) "complete-return/alcoholic-products/Wine/change/spr/multiple-duty-rates/eligible-volume"
        else "complete-return/alcoholic-products/Wine/declare/check-your-answers"}": String))

  /*Spirits pages*/
  def getWhatDoYouNeedToDeclareSpiritsPage: HttpRequestBuilder =
    http("Navigate to What Do You Need To Declare Spirits Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/products-to-include": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What do you need to declare?"))

  def postWhatDoYouNeedToDeclareSpiritsPage: HttpRequestBuilder =
    http("Post What Do You Need To Declare Spirits Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/products-to-include")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("rateBand[315]", "315")
      .formParam("rateBand[325]", "325")
      .formParam("rateBand[335]", "335")
      .formParam("rateBand[345]", "345")
      .formParam("rateBand[355]", "355")
      .formParam("rateBand[360]", "360")
      .formParam("rateBand[365]", "365")
      .formParam("rateBand[370]", "370")
      .formParam("rateBand[375]", "375")
      .formParam("rateBand[380]", "380")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Spirits/declare/volumes": String))

  def getHowMuchYouNeedToDeclareSpiritsPage: HttpRequestBuilder =
    http("Navigate to How Much You Need To Declare Spirits Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the spirits you need to declare"))

  def postHowMuchYouNeedToDeclareSpiritsPage: HttpRequestBuilder =
    http("Post How Much You Need To Declare Spirits Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumes[0].totalLitres", 945.55)
      .formParam("volumes[0].pureAlcohol", 55.5555)
      .formParam("volumes[0].taxType", 315)
      .formParam("volumes[1].totalLitres", 898.34)
      .formParam("volumes[1].pureAlcohol", 77.55)
      .formParam("volumes[1].taxType", 325)
      .formParam("volumes[2].totalLitres", 667.32)
      .formParam("volumes[2].pureAlcohol", 66.34)
      .formParam("volumes[2].taxType", 335)
      .formParam("volumes[3].totalLitres", 999.19)
      .formParam("volumes[3].pureAlcohol", 99.13)
      .formParam("volumes[3].taxType", 345)
      .formParam("volumes[4].totalLitres", 887.54)
      .formParam("volumes[4].pureAlcohol", 66.44)
      .formParam("volumes[4].taxType", 355)
      .formParam("volumes[5].totalLitres", 699.45)
      .formParam("volumes[5].pureAlcohol", 66.89)
      .formParam("volumes[5].taxType", 360)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Spirits/declare/spr/multiple-duty-rates": String))

  def getDoYouHaveMultipleSprDutyRateSpiritsPage: HttpRequestBuilder =
    http("Navigate to Do You Have Multiple Small Producer Relief Duty Rate Spirits Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/spr/multiple-duty-rates": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("If you have more than one Small Producer Relief duty rate"))

  def postDoYouHaveMultipleSprDutyRateSpiritsPage(multipleSprDutyRateQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Do You Have Multiple Small Producer Relief Duty Rate Spirits Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/spr/multiple-duty-rates")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("doYouHaveMultipleSPRDutyRates-yesNoValue", multipleSprDutyRateQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (multipleSprDutyRateQuestion) "complete-return/alcoholic-products/Spirits/declare/spr/multiple-duty-rates/eligible-volume"
        else "complete-return/alcoholic-products/Spirits/declare/spr/eligible-volumes"}": String))

  def getSingleSprRateSpiritsPage: HttpRequestBuilder =
    http("Navigate to Single Small Producer Relief Rate Spirits Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/spr/eligible-volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the spirits that are eligible for Small Producer Relief"))

  def postSingleSprRateSpiritsPage: HttpRequestBuilder =
    http("Post Single Small Producer Relief Rate Spirits Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/spr/eligible-volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumesWithRate[0].totalLitres", 888.88)
      .formParam("volumesWithRate[0].pureAlcohol", 99.45)
      .formParam("volumesWithRate[0].dutyRate", 15)
      .formParam("volumesWithRate[0].taxType", 365)
      .formParam("volumesWithRate[1].totalLitres", 776.45)
      .formParam("volumesWithRate[1].pureAlcohol", 78.9)
      .formParam("volumesWithRate[1].dutyRate", 18)
      .formParam("volumesWithRate[1].taxType", 370)
      .formParam("volumesWithRate[2].totalLitres", 776.89)
      .formParam("volumesWithRate[2].pureAlcohol", 99.99)
      .formParam("volumesWithRate[2].dutyRate", 15)
      .formParam("volumesWithRate[2].taxType", 375)
      .formParam("volumesWithRate[3].totalLitres", 889.65)
      .formParam("volumesWithRate[3].pureAlcohol", 66.54)
      .formParam("volumesWithRate[3].dutyRate", 20)
      .formParam("volumesWithRate[3].taxType", 380)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Spirits/declare/check-your-answers": String))

  def getCheckYourAnswersReturnsSpiritsPage: HttpRequestBuilder =
    http("Navigate to Check Your Answers Returns Spirits Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/check-your-answers": String)
      .check(status.is(200))
      .check(regex("Check your answers"))

  def getDutyDueSpiritsPage(dutyDueAmount: String): HttpRequestBuilder =
    http("Navigate to Duty Due Spirits Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/duty-due": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("You will owe " + dutyDueAmount))

  def postDutyDueSpiritsPage: HttpRequestBuilder =
    http("Post Duty Due Spirits Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/duty-due")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/task-list": String))

  /*Spirits - SPR Yes pages*/
  def getMultipleSprRateSpiritsPage: HttpRequestBuilder =
    http("Navigate to Multiple Small Producer Relief Rate Spirits Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/spr/multiple-duty-rates/eligible-volume": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the spirits you need to declare that are eligible for Small Producer Relief"))

  def postMultipleSprRateSpiritsPage: HttpRequestBuilder =
    http("Post Multiple Small Producer Relief Rate Spirits Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/spr/multiple-duty-rates/eligible-volume")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumesWithRate.taxType", "365")
      .formParam("volumesWithRate.totalLitres", 9999.99)
      .formParam("volumesWithRate.pureAlcohol", 89.9999)
      .formParam("volumesWithRate.dutyRate", 19)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Spirits/declare/spr/check-your-answers": String))

  def getCheckYourAnswersSprSpiritsPage: HttpRequestBuilder =
    http("Navigate to Check Your Answers SPR Spirits Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/spr/check-your-answers": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Check your answers"))

  def postCheckYourAnswersSprSpiritsPage: HttpRequestBuilder =
    http("Post Check Your Answers SPR Spirits Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/spr/check-your-answers")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/Spirits/declare/spr/multiple-duty-rates/eligible-volumes": String))

  def getMultipleSprListQuestionSpiritsPage: HttpRequestBuilder =
    http("Navigate to Multiple SPR List Question Spirits Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/spr/multiple-duty-rates/eligible-volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Spirits with Small Producer Relief duty to declare"))

  def postMultipleSprListQuestionSpiritsPage(multipleSprListQuestion: Boolean = true): HttpRequestBuilder =
    http("Post Multiple SPR List Question Spirits Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/Spirits/declare/spr/multiple-duty-rates/eligible-volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("multipleSPRList-yesNoValue", multipleSprListQuestion)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/${if (multipleSprListQuestion) "complete-return/alcoholic-products/Spirits/change/spr/multiple-duty-rates/eligible-volume"
        else "complete-return/alcoholic-products/Spirits/declare/check-your-answers"}": String))

  /*Other Fermented Product pages*/
  def getWhatDoYouNeedToDeclareOtherFermentedProductPage: HttpRequestBuilder =
    http("Navigate to What Do You Need To Declare Other Fermented Product Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/products-to-include": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("What do you need to declare?"))

  def postWhatDoYouNeedToDeclareOtherFermentedProductPage: HttpRequestBuilder =
    http("Post What Do You Need To Declare Other Fermented Product Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/products-to-include")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("rateBand[314]", "314")
      .formParam("rateBand[324]", "324")
      .formParam("rateBand[334]", "334")
      .formParam("rateBand[344]", "344")
      .formParam("rateBand[354]", "354")
      .formParam("rateBand[359]", "359")
      .formParam("rateBand[364]", "364")
      .formParam("rateBand[369]", "369")
      .formParam("rateBand[374]", "374")
      .formParam("rateBand[379]", "379")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/volumes": String))

  def getHowMuchYouNeedToDeclareOtherFermentedProductPage: HttpRequestBuilder =
    http("Navigate to How Much You Need To Declare Other Fermented Product Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about other fermented products you need to declare"))

  def postHowMuchYouNeedToDeclareOtherFermentedProductPage: HttpRequestBuilder =
    http("Post How Much You Need To Declare Other Fermented Product Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumes[0].totalLitres", 945.55)
      .formParam("volumes[0].pureAlcohol", 55.5555)
      .formParam("volumes[0].taxType", 314)
      .formParam("volumes[1].totalLitres", 898.34)
      .formParam("volumes[1].pureAlcohol", 77.55)
      .formParam("volumes[1].taxType", 324)
      .formParam("volumes[2].totalLitres", 667.32)
      .formParam("volumes[2].pureAlcohol", 66.34)
      .formParam("volumes[2].taxType", 334)
      .formParam("volumes[3].totalLitres", 999.19)
      .formParam("volumes[3].pureAlcohol", 99.13)
      .formParam("volumes[3].taxType", 344)
      .formParam("volumes[4].totalLitres", 887.54)
      .formParam("volumes[4].pureAlcohol", 66.44)
      .formParam("volumes[4].taxType", 354)
      .formParam("volumes[5].totalLitres", 699.45)
      .formParam("volumes[5].pureAlcohol", 66.89)
      .formParam("volumes[5].taxType", 359)
      .check(status.is(303))
      .check(
        header("Location")
          .is(s"/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/multiple-duty-rates": String)
      )

  def getDoYouHaveMultipleSprDutyRateOtherFermentedProductPage: HttpRequestBuilder =
    http("Navigate to Do You Have Multiple Small Producer Relief Duty Rate Other Fermented Product Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/multiple-duty-rates": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("If you have more than one Small Producer Relief duty rate"))

  def postDoYouHaveMultipleSprDutyRateOtherFermentedProductPage(
    multipleSprDutyRateQuestion: Boolean = true
  ): HttpRequestBuilder =
    http("Post Do You Have Multiple Small Producer Relief Duty Rate Other Fermented Product Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/multiple-duty-rates")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("doYouHaveMultipleSPRDutyRates-yesNoValue", multipleSprDutyRateQuestion)
      .check(status.is(303))
      .check(
        header("Location").is(s"/$route/${if (multipleSprDutyRateQuestion) "complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/multiple-duty-rates/eligible-volume"
          else "complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/eligible-volumes"}": String)
      )

  def getSingleSprRateOtherFermentedProductPage: HttpRequestBuilder =
    http("Navigate to Single Small Producer Relief Rate Other Fermented Product Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/eligible-volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Tell us about the other fermented product that is eligible for Small Producer Relief"))

  def postSingleSprRateOtherFermentedProductPage: HttpRequestBuilder =
    http("Post Single Small Producer Relief Rate Other Fermented Product Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/eligible-volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumesWithRate[0].totalLitres", 888.88)
      .formParam("volumesWithRate[0].pureAlcohol", 99.45)
      .formParam("volumesWithRate[0].dutyRate", 15)
      .formParam("volumesWithRate[0].taxType", 364)
      .formParam("volumesWithRate[1].totalLitres", 776.45)
      .formParam("volumesWithRate[1].pureAlcohol", 78.9)
      .formParam("volumesWithRate[1].dutyRate", 18)
      .formParam("volumesWithRate[1].taxType", 369)
      .formParam("volumesWithRate[2].totalLitres", 776.89)
      .formParam("volumesWithRate[2].pureAlcohol", 99.99)
      .formParam("volumesWithRate[2].dutyRate", 15)
      .formParam("volumesWithRate[2].taxType", 374)
      .formParam("volumesWithRate[3].totalLitres", 889.65)
      .formParam("volumesWithRate[3].pureAlcohol", 66.54)
      .formParam("volumesWithRate[3].dutyRate", 20)
      .formParam("volumesWithRate[3].taxType", 379)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/check-your-answers": String))

  def getCheckYourAnswersReturnsOtherFermentedProductPage: HttpRequestBuilder =
    http("Navigate to Check Your Answers Returns Other Fermented Product Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/check-your-answers": String)
      .check(status.is(200))
      .check(regex("Check your answers"))

  def getDutyDueOtherFermentedProductPage(dutyDueAmount: String): HttpRequestBuilder =
    http("Navigate to Duty Due Other Fermented Product Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/duty-due": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("You will owe " + dutyDueAmount))

  def postDutyDueOtherFermentedProductPage: HttpRequestBuilder =
    http("Post Duty Due Other Fermented Product Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/duty-due")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/task-list": String))

  /*Other Fermented Products - SPR Yes pages*/
  def getMultipleSprRateOtherFermentedProductPage: HttpRequestBuilder =
    http("Navigate to Multiple Small Producer Relief Rate Other Fermented Product Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/multiple-duty-rates/eligible-volume": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(
        regex(
          "Tell us about the other fermented products you need to declare that are eligible for Small Producer Relief"
        )
      )

  def postMultipleSprRateOtherFermentedProductPage: HttpRequestBuilder =
    http("Post Multiple Small Producer Relief Rate Other Fermented Product Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/multiple-duty-rates/eligible-volume")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("volumesWithRate.taxType", "364")
      .formParam("volumesWithRate.totalLitres", 9999.99)
      .formParam("volumesWithRate.pureAlcohol", 89.9999)
      .formParam("volumesWithRate.dutyRate", 19)
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/check-your-answers": String))

  def getCheckYourAnswersSprOtherFermentedProductPage: HttpRequestBuilder =
    http("Navigate to Check Your Answers SPR Other Fermented Product Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/check-your-answers": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Check your answers"))

  def postCheckYourAnswersSprOtherFermentedProductPage: HttpRequestBuilder =
    http("Post Check Your Answers SPR Other Fermented Product Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/check-your-answers")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/multiple-duty-rates/eligible-volumes": String))

  def getMultipleSprListQuestionOtherFermentedProductPage: HttpRequestBuilder =
    http("Navigate to Multiple SPR List Question Other Fermented Product Page")
      .get(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/multiple-duty-rates/eligible-volumes": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("Other fermented products with Small Producer Relief duty to declare"))

  def postMultipleSprListQuestionOtherFermentedProductPage(
    multipleSprListQuestion: Boolean = true
  ): HttpRequestBuilder =
    http("Post Multiple SPR List Question Other Fermented Product Page")
      .post(s"$baseUrl/$route/complete-return/alcoholic-products/OtherFermentedProduct/declare/spr/multiple-duty-rates/eligible-volumes")
      .formParam("csrfToken", "${csrfToken}")
      .formParam("multipleSPRList-yesNoValue", multipleSprListQuestion)
      .check(status.is(303))
      .check(
        header("Location").is(s"/$route/${if (multipleSprListQuestion) "complete-return/alcoholic-products/OtherFermentedProduct/change/spr/multiple-duty-rates/eligible-volume"
          else "complete-return/alcoholic-products/OtherFermentedProduct/declare/check-your-answers"}": String)
      )

  def getReturnSummary(dutyDueAmount: String): HttpRequestBuilder =
    http("Navigate to Return Summary Page")
      .get(s"$baseUrl/$route/complete-return/check-return": String)
      .check(status.is(200))
      .check(saveCsrfToken())
      .check(regex("The duty due for this return is " + dutyDueAmount))

  def postReturnSummary: HttpRequestBuilder =
    http("Post Return Summary Page")
      .post(s"$baseUrl/$route/complete-return/check-return")
      .formParam("csrfToken", "${csrfToken}")
      .check(status.is(303))
      .check(header("Location").is(s"/$route/complete-return/return-sent": String))

  def getReturnSubmitted: HttpRequestBuilder =
    http("Navigate to Return Submitted Page")
      .get(s"$baseUrl/$route/complete-return/return-sent": String)
      .check(status.is(200))
      .check(regex("Return sent"))
}
