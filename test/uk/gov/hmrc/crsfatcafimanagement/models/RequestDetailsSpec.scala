/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.crsfatcafimanagement.models

import play.api.libs.json.Json
import uk.gov.hmrc.crsfatcafimanagement.SpecBase
import uk.gov.hmrc.crsfatcafimanagement.models.CADXRequestModels.{CreateRequestDetails, UpdateRequestDetails}

class RequestDetailsSpec extends SpecBase {

  "RequestDetails" - {
    "CreateRequestDetails should serialize" in {

      val createRequestDetails = CreateRequestDetails(
        "FIName",
        "SubscriptionID",
        List.empty,
        IsFIUser = true,
        AddressDetails(
          "AddressLine1",
          None,
          "AddressLine3",
          None,
          Some("GB"),
          Some("AA1 1AA")
        ),
        Some(
          ContactDetails(
            "ContactName",
            "EmailAddress",
            Some("PhoneNumber")
          )
        )
      )

      val json = Json.toJson(createRequestDetails)
      val expectedJson = Json.parse("""
          |{
          |  "FIName" : "FIName",
          |  "SubscriptionID" : "SubscriptionID",
          |  "TINDetails" : [ ],
          |  "IsFIUser" : true,
          |  "AddressDetails" : {
          |    "AddressLine1" : "AddressLine1",
          |    "AddressLine3" : "AddressLine3",
          |    "CountryCode" : "GB",
          |    "PostalCode" : "AA1 1AA"
          |  },
          |  "PrimaryContactDetails" : {
          |    "ContactName" : "ContactName",
          |    "EmailAddress" : "EmailAddress",
          |    "PhoneNumber" : "PhoneNumber"
          |  }
          |}
          |""".stripMargin)

      json mustBe expectedJson

    }

    "UpdateRequestDetails should serialize" in {
      val request = UpdateRequestDetails(
        "FIID",
        "FIName",
        "SubscriptionID",
        List.empty,
        IsFIUser = true,
        AddressDetails(
          "AddressLine1",
          None,
          "AddressLine3",
          None,
          Some("GB"),
          Some("AA1 1AA")
        ),
        Some(
          ContactDetails(
            "ContactName",
            "EmailAddress",
            Some("PhoneNumber")
          )
        )
      )

      val json = Json.toJson(request)
      val expectedJson = Json.parse("""
          |{
          |  "FIID": "FIID",
          |  "FIName": "FIName",
          |  "SubscriptionID": "SubscriptionID",
          |  "TINDetails": [],
          |  "IsFIUser": true,
          |  "AddressDetails": {
          |    "AddressLine1": "AddressLine1",
          |    "AddressLine3": "AddressLine3",
          |    "CountryCode": "GB",
          |    "PostalCode": "AA1 1AA"
          |  },
          |  "PrimaryContactDetails": {
          |    "ContactName": "ContactName",
          |    "EmailAddress": "EmailAddress",
          |    "PhoneNumber": "PhoneNumber"
          |  }
          |}
          |""".stripMargin)

      json mustBe expectedJson
    }
  }

}
