/*
 * Copyright 2026 HM Revenue & Customs
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

import org.mockito.Mockito.{mockingDetails, reset}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.crsfatcafimanagement.models.CADXRequestModels.CreateRequestDetails
import uk.gov.hmrc.crsfatcafimanagement.models.audit.{AddFinancialInstitution, RemoveFinancialInstitution}
import uk.gov.hmrc.crsfatcafimanagement.models.{AddressDetails, ContactDetails, TINDetails, TINType}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.DefaultAuditConnector

import scala.concurrent.ExecutionContext

class AuditServiceSpec extends AnyFreeSpec with Matchers with MockitoSugar with BeforeAndAfterEach {

  implicit private val ec: ExecutionContext =
    ExecutionContext.global

  implicit private val hc: HeaderCarrier =
    HeaderCarrier()

  private val mockAuditConnector =
    mock[DefaultAuditConnector]

  private val service =
    new AuditService(mockAuditConnector)

  override def beforeEach(): Unit = {
    reset(mockAuditConnector)
    super.beforeEach()
  }

  "AuditService" - {

    "sendAddFinancialInstitution" - {

      "must send the correct AddFinancialInstitution audit event" in {

        val request =
          CreateRequestDetails(
            FIName = "Test Financial Institution",
            SubscriptionID = "FATCA123456",
            TINDetails = List(
              TINDetails(
                TINType = TINType.UTR,
                TIN = "1234567890",
                IssuedBy = "GB"
              ),
              TINDetails(
                TINType = TINType.CRN,
                TIN = "12345678",
                IssuedBy = "GB"
              ),
              TINDetails(
                TINType = TINType.TURN,
                TIN = "TURN123456",
                IssuedBy = "GB"
              )
            ),
            GIIN = Some("GIIN123456"),
            IsFIUser = true,
            AddressDetails = AddressDetails(
              AddressLine1 = "1 Test Street",
              AddressLine2 = Some("Test Area"),
              AddressLine3 = Some("Test City"),
              AddressLine4 = Some("Test County"),
              PostalCode = Some("TE1 1ST"),
              CountryCode = Some("GB")
            ),
            PrimaryContactDetails = Some(
              ContactDetails(
                ContactName = "Primary Contact",
                EmailAddress = "primary@example.com",
                PhoneNumber = Some("01234567890")
              )
            ),
            SecondaryContactDetails = Some(
              ContactDetails(
                ContactName = "Secondary Contact",
                EmailAddress = "secondary@example.com",
                PhoneNumber = Some("09876543210")
              )
            )
          )

        val financialInstitutionId = "FI123456"

        service.sendAddFinancialInstitution(
          request = request,
          financialInstitutionId = financialInstitutionId
        )

        val invocations =
          mockingDetails(mockAuditConnector).getInvocations

        invocations.size() mustBe 1

        val invocation =
          invocations.iterator().next()

        invocation.getArgument[String](0) mustBe
          "AddFinancialInstitution"

        val event =
          invocation.getArgument[AddFinancialInstitution](1)

        event.fatcaId mustBe "FATCA123456"
        event.isRegisteredBusiness mustBe true
        event.financialInstitutionId mustBe "FI123456"
        event.financialInstitutionName mustBe "Test Financial Institution"

        event.utr mustBe Some("1234567890")
        event.crn mustBe Some("12345678")
        event.urn mustBe Some("TURN123456")

        event.giin mustBe Some("GIIN123456")

        event.addressLine1 mustBe "1 Test Street"
        event.addressLine2 mustBe Some("Test Area")
        event.city mustBe Some("Test City")
        event.county mustBe Some("Test County")
        event.postcode mustBe Some("TE1 1ST")
        event.country mustBe Some("GB")

        event.uprn mustBe None

        event.primaryContactName mustBe Some("Primary Contact")
        event.primaryContactEmail mustBe Some("primary@example.com")
        event.primaryContactTelephone mustBe Some("01234567890")

        event.secondaryContactName mustBe Some("Secondary Contact")
        event.secondaryContactEmail mustBe Some("secondary@example.com")
        event.secondaryContactTelephone mustBe Some("09876543210")
      }

      "must only populate the TIN field matching the supplied TIN type" in {

        val request =
          CreateRequestDetails(
            FIName = "Test Financial Institution",
            SubscriptionID = "FATCA123456",
            TINDetails = List(
              TINDetails(
                TINType = TINType.CRN,
                TIN = "12345678",
                IssuedBy = "GB"
              )
            ),
            GIIN = None,
            IsFIUser = false,
            AddressDetails = AddressDetails(
              AddressLine1 = "1 Test Street",
              AddressLine2 = None,
              AddressLine3 = None,
              AddressLine4 = None,
              PostalCode = Some("TE1 1ST"),
              CountryCode = Some("GB")
            ),
            PrimaryContactDetails = None,
            SecondaryContactDetails = None
          )

        service.sendAddFinancialInstitution(
          request = request,
          financialInstitutionId = "FI123456"
        )

        val invocations =
          mockingDetails(mockAuditConnector).getInvocations

        invocations.size() mustBe 1

        val invocation =
          invocations.iterator().next()

        val event =
          invocation.getArgument[AddFinancialInstitution](1)

        event.utr mustBe None
        event.crn mustBe Some("12345678")
        event.urn mustBe None
      }
    }

    "sendRemoveFinancialInstitution" - {

      "must send the correct RemoveFinancialInstitution audit event" in {

        val expectedEvent =
          RemoveFinancialInstitution(
            financialInstitutionId = "FI123456",
            fatcaId = "FATCA123456"
          )

        service.sendRemoveFinancialInstitution(
          financialInstitutionId = expectedEvent.financialInstitutionId,
          fatcaId = expectedEvent.fatcaId
        )

        val invocations =
          mockingDetails(mockAuditConnector).getInvocations

        invocations.size() mustBe 1

        val invocation =
          invocations.iterator().next()

        invocation.getArgument[String](0) mustBe
          "RemoveFinancialInstitution"

        invocation.getArgument[RemoveFinancialInstitution](1) mustBe
          expectedEvent
      }
    }
  }

}
