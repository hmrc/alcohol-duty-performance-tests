/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.perftests.example

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

object AlcoholDutyRequests extends ServicesConfiguration {

  val baseUrl: String = baseUrlFor("manage-alcohol-duty")
  val route: String   = "/productName"

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
