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
