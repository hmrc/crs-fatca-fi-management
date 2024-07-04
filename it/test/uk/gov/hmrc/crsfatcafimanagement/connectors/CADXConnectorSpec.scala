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

package uk.gov.hmrc.crsfatcafimanagement.connectors

import com.github.tomakehurst.wiremock.http.RequestMethod
import com.softwaremill.quicklens._
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.concurrent.IntegrationPatience
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.{Application, Configuration}
import uk.gov.hmrc.crsfatcafimanagement.SpecBase
import uk.gov.hmrc.crsfatcafimanagement.generators.Generators
import uk.gov.hmrc.crsfatcafimanagement.models.{FIDetail, ViewFIDetailsResponse}
import uk.gov.hmrc.crsfatcafimanagement.wiremock.WireMockHelper

import scala.concurrent.ExecutionContext.Implicits.global

class CADXConnectorSpec extends SpecBase with Generators with IntegrationPatience with WireMockHelper {

  override def beforeAll(): Unit = {
    super.beforeAll()
    startWireMock()
  }

  override def afterAll(): Unit = {
    stopWireMock()
    super.afterAll()
  }

  override lazy val app: Application = applicationBuilder()
    .configure(Configuration("microservice.services.get-financial-institutions.port" -> wireMockServer.port()))
    .build()

  lazy val connector: CADXConnector = app.injector.instanceOf[CADXConnector]

  private val errorStatusCodes = Table(
    "errorStatus",
    FORBIDDEN,
    NOT_FOUND,
    METHOD_NOT_ALLOWED,
    UNPROCESSABLE_ENTITY,
    INTERNAL_SERVER_ERROR,
    SERVICE_UNAVAILABLE
  )

  "CADXConnector" - {
    "listFinancialInstitutions" - {
      "must return status OK when listing FIs" in {

        forAll(arbitrary[ViewFIDetailsResponse], arbitrary[FIDetail]) {
          (response, fiDetail) =>
            val subscriptionId  = fiDetail.subscriptionID
            val stubbedResponse = response.modify(_.viewFIDetails.responseDetails.fIDetails).setTo(List(fiDetail))

            stubResponse(
              url = s"/ASMService/v1/VIEW/$subscriptionId",
              statusCode = OK,
              requestMethod = RequestMethod.GET,
              requestHeaders = Map.empty,
              responseBody = Json.prettyPrint(Json.toJson(stubbedResponse))
            )

            val result = connector.listFinancialInstitutions(subscriptionId).futureValue
            result.status mustBe OK
            Json.parse(result.body).as[ViewFIDetailsResponse] mustBe stubbedResponse
        }
      }

      forAll(errorStatusCodes) {
        errorStatusCode =>
          s"must return status $errorStatusCode when CADX returns $errorStatusCode" in {

            forAll(arbitrary[FIDetail]) {
              fiDetail =>
                val subscriptionId = fiDetail.subscriptionID

                stubResponse(
                  url = s"/ASMService/v1/VIEW/$subscriptionId",
                  statusCode = errorStatusCode,
                  requestMethod = RequestMethod.GET,
                  requestHeaders = Map.empty
                )

                val result = connector.listFinancialInstitutions(subscriptionId).futureValue
                result.status mustBe errorStatusCode
            }
          }
      }
    }

    "viewFinancialInstitution" - {
      "must return status OK when viewing an FI" in {

        forAll(arbitrary[ViewFIDetailsResponse], arbitrary[FIDetail]) {
          (response, fiDetail) =>
            val subscriptionId  = fiDetail.subscriptionID
            val fiId            = fiDetail.fIID
            val stubbedResponse = response.modify(_.viewFIDetails.responseDetails.fIDetails).setTo(List(fiDetail))

            stubResponse(
              url = s"/ASMService/v1/VIEW/$subscriptionId/$fiId",
              statusCode = OK,
              requestMethod = RequestMethod.GET,
              requestHeaders = Map.empty,
              responseBody = Json.prettyPrint(Json.toJson(stubbedResponse))
            )

            val result = connector.viewFinancialInstitution(subscriptionId, fiId).futureValue
            result.status mustBe OK
            Json.parse(result.body).as[ViewFIDetailsResponse] mustBe stubbedResponse
        }
      }

      forAll(errorStatusCodes) {
        errorStatusCode =>
          s"must return status $errorStatusCode when CADX returns $errorStatusCode" in {

            forAll(arbitrary[FIDetail]) {
              fiDetail =>
                val subscriptionId = fiDetail.subscriptionID
                val fiId           = fiDetail.fIID

                stubResponse(
                  url = s"/ASMService/v1/VIEW/$subscriptionId/$fiId",
                  statusCode = errorStatusCode,
                  requestMethod = RequestMethod.GET,
                  requestHeaders = Map.empty
                )

                val result = connector.viewFinancialInstitution(subscriptionId, fiId).futureValue
                result.status mustBe errorStatusCode
            }
          }
      }
    }
  }

}
