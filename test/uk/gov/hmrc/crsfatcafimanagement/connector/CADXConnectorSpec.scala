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

package uk.gov.hmrc.crsfatcafimanagement.connector

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, urlEqualTo}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.http.Status.OK
import uk.gov.hmrc.crsfatcafimanagement.connectors.CADXConnector
import uk.gov.hmrc.crsfatcafimanagement.generators.Generators
import uk.gov.hmrc.crsfatcafimanagement.models.CADXRequestModels.{
  CreateRequestDetails,
  FIDetailsRequest,
  FIManagement,
  RemoveFIDetailsRequest,
  UpdateRequestDetails
}
import uk.gov.hmrc.crsfatcafimanagement.{SpecBase, WireMockServerHandler}

import scala.concurrent.ExecutionContext.Implicits.global

class CADXConnectorSpec extends SpecBase with WireMockServerHandler with Generators with ScalaCheckPropertyChecks {

  override lazy val app: Application = applicationBuilder()
    .configure(
      conf = "microservice.services.submission.port" -> server.port(),
      "microservice.services.submission.bearer-token" -> "local-token",
      "auditing.enabled"                              -> "false"
    )
    .build()

  lazy val connector: CADXConnector =
    app.injector.instanceOf[CADXConnector]

  private val errorCodes: Gen[Int] = Gen.chooseNum(400, 599)

  "CADXConnector" - {

    "create FI" - {
      "must return status as OK for update Subscription" in {
        stubResponse(
          "/ASMService/v1/FIManagement",
          OK
        )

        forAll(arbitrary[FIDetailsRequest[CreateRequestDetails]]) {
          sub =>
            val result = connector.createFI(FIManagement(sub))
            result.futureValue.status mustBe OK
        }
      }

      "must return an error status for failed update Subscription" in {

        forAll(arbitrary[FIDetailsRequest[CreateRequestDetails]], errorCodes) {
          (sub, errorCode) =>
            stubResponse(
              "/ASMService/v1/FIManagement",
              errorCode
            )

            val result = connector.createFI(FIManagement(sub))
            result.futureValue.status mustBe errorCode
        }
      }
    }
    "update FI" - {
      "must return status as OK for update Subscription" in {
        stubResponse(
          "/ASMService/v1/FIManagement",
          OK
        )

        forAll(arbitrary[FIDetailsRequest[UpdateRequestDetails]]) {
          sub =>
            val result = connector.createFI(FIManagement(sub))
            result.futureValue.status mustBe OK
        }
      }

      "must return an error status for failed update Subscription" in {

        forAll(arbitrary[FIDetailsRequest[UpdateRequestDetails]], errorCodes) {
          (sub, errorCode) =>
            stubResponse(
              "/ASMService/v1/FIManagement",
              errorCode
            )

            val result = connector.createFI(FIManagement(sub))
            result.futureValue.status mustBe errorCode
        }
      }
    }
    "remove FI" - {
      "must return status as OK for removal request" in {
        stubResponse(
          "/ASMService/v1/FIManagement",
          OK
        )

        forAll(arbitrary[RemoveFIDetailsRequest]) {
          req =>
            val result = connector.removeFI(FIManagement(req))
            result.futureValue.status mustBe OK
        }
      }

      "must return an error status for failed remove request" in {

        forAll(arbitrary[RemoveFIDetailsRequest], errorCodes) {
          (req, errorCode) =>
            stubResponse(
              "/ASMService/v1/FIManagement",
              errorCode
            )

            val result = connector.removeFI(FIManagement(req))
            result.futureValue.status mustBe errorCode
        }
      }
    }

  }

  private def stubResponse(
    expectedUrl: String,
    expectedStatus: Int
  ): StubMapping =
    server.stubFor(
      post(urlEqualTo(expectedUrl))
        .willReturn(
          aResponse()
            .withStatus(expectedStatus)
        )
    )

}
