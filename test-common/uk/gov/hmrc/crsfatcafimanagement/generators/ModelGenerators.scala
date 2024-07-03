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

package uk.gov.hmrc.crsfatcafimanagement.generators

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen.listOf
import org.scalacheck.{Arbitrary, Gen}
import uk.gov.hmrc.crsfatcafimanagement.models._

trait ModelGenerators {
  self: Generators =>

  implicit val arbitraryAddressDetails: Arbitrary[AddressDetails] = Arbitrary {
    for {
      addressLine1 <- stringOfLength(35)
      addressLine2 <- stringOfLength(35)
      addressLine3 <- stringOfLength(35)
      addressLine4 <- Gen.option(stringOfLength(35))
      postalCode   <- Gen.option(stringOfLength(10))
      countryCode  <- Gen.option(stringOfLength(2))
    } yield AddressDetails(
      addressLine1,
      addressLine2,
      addressLine3,
      addressLine4,
      postalCode,
      countryCode
    )
  }

  implicit val arbitraryContactDetails: Arbitrary[ContactDetails] =
    Arbitrary {
      for {
        contactName  <- stringOfLength(105)
        emailAddress <- validPhoneNumber
        phoneNumber  <- stringOfLength(24)
      } yield ContactDetails(contactName, emailAddress, phoneNumber)
    }

  implicit val arbitraryTINDetails: Arbitrary[TINDetails] =
    Arbitrary {
      for {
        tinType  <- Gen.oneOf(TINType.values)
        tin      <- stringOfLength(25)
        issuedBy <- stringOfLength(2)
      } yield TINDetails(tinType, tin, issuedBy.toUpperCase)
    }

  implicit val arbitraryFIDetail: Arbitrary[FIDetail] = Arbitrary {
    for {
      fiId                    <- stringOfLength(15)
      fiName                  <- stringOfLength(105)
      subscriptionId          <- validSubscriptionID
      tinDetails              <- arbitrary[TINDetails]
      isFIUser                <- arbitrary[Boolean]
      isFATCAReporting        <- arbitrary[Boolean]
      addressDetails          <- arbitrary[AddressDetails]
      primaryContactDetails   <- arbitrary[ContactDetails]
      secondaryContactDetails <- arbitrary[ContactDetails]
    } yield FIDetail(
      FIID = fiId,
      FIName = fiName,
      SubscriptionID = subscriptionId,
      TINDetails = tinDetails,
      IsFIUser = isFIUser,
      IsFATCAReporting = isFATCAReporting,
      AddressDetails = addressDetails,
      PrimaryContactDetails = primaryContactDetails,
      SecondaryContactDetails = secondaryContactDetails
    )
  }

  implicit val arbitraryResponseDetails: Arbitrary[ResponseDetails] = Arbitrary {
    listOf(arbitrary[FIDetail]).flatMap(ResponseDetails.apply)
  }

  implicit val arbitraryResponseParameter: Arbitrary[ResponseParameter] =
    Arbitrary {
      for {
        parameterName  <- stringOfLength(100)
        parameterValue <- stringOfLength(255)
      } yield ResponseParameter(parameterName, parameterValue)
    }

  implicit val arbitraryResponseCommon: Arbitrary[ResponseCommon] = Arbitrary {
    for {
      originatingSystem  <- stringOfLength(30)
      transmittingSystem <- stringOfLength(30)
      requestType = RequestType.VIEW
      regime      = "CRSFATCA"
      responseParameters <- listOf(arbitrary[ResponseParameter])
    } yield ResponseCommon(
      originatingSystem,
      transmittingSystem,
      requestType,
      regime,
      responseParameters
    )
  }

  implicit val arbitraryViewFIDetails: Arbitrary[ViewFIDetails] = Arbitrary {
    for {
      responseCommon  <- arbitrary[ResponseCommon]
      responseDetails <- arbitrary[ResponseDetails]
    } yield ViewFIDetails(responseCommon, responseDetails)
  }

  implicit val arbitraryViewFIDetailsResponse: Arbitrary[ViewFIDetailsResponse] =
    Arbitrary {
      arbitrary[ViewFIDetails].map(ViewFIDetailsResponse.apply)
    }

}