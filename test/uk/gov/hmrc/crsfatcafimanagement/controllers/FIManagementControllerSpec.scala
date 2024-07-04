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

package uk.gov.hmrc.crsfatcafimanagement.controllers

import org.mockito.ArgumentMatchers.any
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.http.Status.OK
import play.api.inject.bind
import play.api.libs.json.{JsValue, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers.{POST, route, status}
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.crsfatcafimanagement.SpecBase
import uk.gov.hmrc.crsfatcafimanagement.auth.{AllowAllAuthAction, FakeAllowAllAuthAction}
import uk.gov.hmrc.crsfatcafimanagement.connectors.CADXConnector
import uk.gov.hmrc.crsfatcafimanagement.generators.Generators
import uk.gov.hmrc.crsfatcafimanagement.models.CADXRequestModels.CreateFIDetailsRequest
import uk.gov.hmrc.crsfatcafimanagement.services.CADXSubmissionService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class FIManagementControllerSpec extends SpecBase with Generators with ScalaCheckPropertyChecks {

  val mockAuthConnector: AuthConnector = mock[AuthConnector]

  val mockCADXConnector: CADXConnector = mock[CADXConnector]
  val mockCADXSubmissionService: CADXSubmissionService = mock[CADXSubmissionService]

  val application: Application = applicationBuilder()
    .overrides(
      bind[CADXConnector].toInstance(mockCADXConnector),
      bind[CADXSubmissionService].toInstance(mockCADXSubmissionService),
      bind[AuthConnector].toInstance(mockAuthConnector),
      bind[AllowAllAuthAction].to[FakeAllowAllAuthAction]
    )
    .build()

  val fiDetailsRequestJson: JsValue = Json.parse(
    """
      |{
      |  "FIManagementType": {
      |    "RequestCommon": {
      |      "TransmittingSystem": "192.168.1.1",
      |      "OriginatingSystem": "192.168.1.2",
      |      "RequestType": "CREATE",
      |      "Regime": "CRSFATCA",
      |      "RequestParameters": [
      |        {
      |          "ParamName": "ExampleParam1",
      |          "ParamValue": "Value1"
      |        },
      |        {
      |          "ParamName": "ExampleParam2",
      |          "ParamValue": "Value2"
      |        }
      |      ]
      |    },
      |    "RequestDetails": {
      |      "SubscriptionID": "123456789012345",
      |      "FIID": "FI1234567890123",
      |      "FIName": "Financial Institution",
      |      "TINDetails": [
      |        {
      |          "TIN": "TIN123456789",
      |          "TINType": "GIIN",
      |          "IssuedBy": "US"
      |        }
      |      ],
      |      "IsFIUser": false,
      |      "IsFATCAReporting": true,
      |      "PrimaryContactDetails": {
      |        "PhoneNumber": "07123456789",
      |        "ContactName": "John Doe",
      |        "EmailAddress": "john.doe@example.com"
      |      },
      |      "SecondaryContactDetails": {
      |        "PhoneNumber": "07876543210",
      |        "ContactName": "Jane Doe",
      |        "EmailAddress": "jane.doe@example.com"
      |      },
      |      "AddressDetails": {
      |        "AddressLine1": "100 Sutton Street",
      |        "AddressLine2": "Wokingham",
      |        "AddressLine3": "Surrey",
      |        "AddressLine4": "London",
      |        "PostalCode": "DH14EJ",
      |        "CountryCode": "GB"
      |      }
      |    }
      |  }
      |}""".stripMargin
  )


  "should return OK when UpdateSubscription was successful" in {
    when(
      mockCADXSubmissionService
        .createFI(any[CreateFIDetailsRequest]())(
          any[HeaderCarrier](),
          any[ExecutionContext]()
        )
    ).thenReturn(
      Future.successful(
        Right(())
      )
    )

    val request =
      FakeRequest(
        POST,
        routes.FIManagementController.createSubmission.url
      ).withJsonBody(fiDetailsRequestJson)

    val result = route(application, request).value
    status(result) mustEqual OK

  }

}
