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

package uk.gov.hmrc.crsfatcafimanagement.services

import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.{any, eq => is}
import org.scalatest.BeforeAndAfterEach
import play.api.http.Status._
import play.api.inject.bind
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import uk.gov.hmrc.crsfatcafimanagement.SpecBase
import uk.gov.hmrc.crsfatcafimanagement.connectors.CADXConnector
import uk.gov.hmrc.crsfatcafimanagement.models.CADXRequestModels.{CreateFIDetails, CreateFIDetailsRequest, RequestCommon, RequestDetails}
import uk.gov.hmrc.crsfatcafimanagement.models.RequestType.CREATE
import uk.gov.hmrc.crsfatcafimanagement.models.errors.CreateSubmissionError
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}

class CADXSubmissionServiceSpec extends SpecBase with BeforeAndAfterEach {

  override def beforeEach(): Unit = reset(mockCADXConnector)

  val mockCADXConnector: CADXConnector = mock[CADXConnector]

  override lazy val app = applicationBuilder()
    .overrides(
      bind[CADXConnector].toInstance(mockCADXConnector)
    )
    .build()

  val fiDetailsRequestJson: JsValue = Json.parse(
    """
      |{
      |  "SubscriptionID": "123456789012345",
      |  "FIID": "FI1234567890123",
      |  "FIName": "Financial Institution",
      |  "TINDetails": [
      |    {
      |      "TIN": "TIN123456789",
      |      "TINType": "GIIN",
      |      "IssuedBy": "US"
      |    }
      |  ],
      |  "IsFIUser": false,
      |  "IsFATCAReporting": true,
      |  "PrimaryContactDetails": {
      |    "PhoneNumber": "07123456789",
      |    "ContactName": "John Doe",
      |    "EmailAddress": "john.doe@example.com"
      |  },
      |  "SecondaryContactDetails": {
      |    "PhoneNumber": "07876543210",
      |    "ContactName": "Jane Doe",
      |    "EmailAddress": "jane.doe@example.com"
      |  },
      |  "AddressDetails": {
      |    "AddressLine1": "100 Sutton Street",
      |    "AddressLine2": "Wokingham",
      |    "AddressLine3": "Surrey",
      |    "AddressLine4": "London",
      |    "PostalCode": "DH14EJ",
      |    "CountryCode": "GB"
      |  }
      |}""".stripMargin
  )

  val createRequestDetails: RequestDetails = fiDetailsRequestJson.as[RequestDetails]

  val createFiDetailsRequest = CreateFIDetailsRequest(
    FIManagementType = CreateFIDetails(
      RequestCommon = RequestCommon(
        OriginatingSystem = "crs-fatca-fi-management",
        TransmittingSystem = "crs-fatca-fi-management",
        RequestType = CREATE,
        Regime = "CRSFATCA",
        RequestParameters = List.empty
      ),
      RequestDetails = createRequestDetails
    )
  )

  "SubmissionService" - {
    "must  return UpdateSubscription with OK status when connector response with ok status" in {
      val service = app.injector.instanceOf[CADXSubmissionService]

      when(mockCADXConnector.createFI(is(createFiDetailsRequest))(any[HeaderCarrier](), any[ExecutionContext]()))
        .thenReturn(Future.successful(HttpResponse(OK, "Good Response")))

      val result = service.createFI(createRequestDetails)

      whenReady(result) {
        sub =>
          verify(mockCADXConnector, times(1)).createFI(is(createFiDetailsRequest))(any[HeaderCarrier](), any[ExecutionContext]())
          sub mustBe Right(())
      }
    }

    "must have UpdateSubscriptionError when connector response with not ok status" in {
      val service = app.injector.instanceOf[CADXSubmissionService]

      when(mockCADXConnector.createFI(is(createFiDetailsRequest))(any[HeaderCarrier](), any[ExecutionContext]()))
        .thenReturn(Future.successful(HttpResponse(INTERNAL_SERVER_ERROR, "")))

      val result = service.createFI(createRequestDetails)

      whenReady(result) {
        sub =>
          verify(mockCADXConnector, times(1)).createFI(is(createFiDetailsRequest))(any[HeaderCarrier](), any[ExecutionContext]())
          sub mustBe Left(CreateSubmissionError(500))
      }
    }
  }

}
