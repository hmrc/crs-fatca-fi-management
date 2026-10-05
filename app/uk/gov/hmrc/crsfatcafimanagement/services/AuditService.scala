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

import play.api.Logger
import play.api.libs.json.OWrites
import uk.gov.hmrc.crsfatcafimanagement.models.CADXRequestModels.{CreateRequestDetailsAllFields, RequestDetails, UpdateRequestDetailsAllFields}
import uk.gov.hmrc.crsfatcafimanagement.models.TINType
import uk.gov.hmrc.crsfatcafimanagement.models.audit.{AddFinancialInstitution, AmendFinancialInstitution, AuditEvent, RemoveFinancialInstitution}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.DefaultAuditConnector

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class AuditService @Inject() (
  auditConnector: DefaultAuditConnector
)(implicit ec: ExecutionContext) {

  private val logger: Logger = Logger(this.getClass)

  def sendAddFinancialInstitution(
    request: CreateRequestDetailsAllFields,
    financialInstitutionId: String
  )(implicit hc: HeaderCarrier): Unit = {

    val event = AddFinancialInstitution(
      fatcaId = request.SubscriptionID,
      isRegisteredBusiness = request.IsFIUser,
      financialInstitutionId = financialInstitutionId,
      financialInstitutionName = request.FIName,
      utr = tinValue(request, TINType.UTR),
      crn = tinValue(request, TINType.CRN),
      urn = tinValue(request, TINType.TURN),
      giin = request.GIIN,
      addressLine1 = request.AddressDetails.AddressLine1,
      addressLine2 = nonEmpty(request.AddressDetails.AddressLine2),
      city = nonEmpty(request.AddressDetails.AddressLine3),
      county = nonEmpty(request.AddressDetails.AddressLine4),
      postcode = nonEmpty(request.AddressDetails.PostalCode),
      country = nonEmpty(request.AddressDetails.CountryCode),
      uprn = request.AddressDetails.Uprn.map(_.toString),
      primaryContactName = request.PrimaryContactDetails.flatMap(
        contact => nonEmpty(contact.ContactName)
      ),
      primaryContactEmail = request.PrimaryContactDetails.flatMap(
        contact => nonEmpty(contact.EmailAddress)
      ),
      primaryContactTelephone = request.PrimaryContactDetails.flatMap(
        contact => nonEmpty(contact.PhoneNumber)
      ),
      secondaryContactName = request.SecondaryContactDetails.flatMap(
        contact => nonEmpty(contact.ContactName)
      ),
      secondaryContactEmail = request.SecondaryContactDetails.flatMap(
        contact => nonEmpty(contact.EmailAddress)
      ),
      secondaryContactTelephone = request.SecondaryContactDetails.flatMap(
        contact => nonEmpty(contact.PhoneNumber)
      )
    )

    send(
      auditType = "AddFinancialInstitution",
      event = event
    )
  }

  def sendAmendFinancialInstitution(
    request: UpdateRequestDetailsAllFields
  )(implicit hc: HeaderCarrier): Unit = {

    val event = AmendFinancialInstitution(
      fatcaId = request.SubscriptionID,
      isRegisteredBusiness = Some(request.IsFIUser),
      financialInstitutionId = request.FIID,
      financialInstitutionName = Some(request.FIName),
      utr = tinValue(request, TINType.UTR),
      crn = tinValue(request, TINType.CRN),
      urn = tinValue(request, TINType.TURN),
      giin = request.GIIN,
      addressLine1 = nonEmpty(request.AddressDetails.AddressLine1),
      addressLine2 = nonEmpty(request.AddressDetails.AddressLine2),
      city = nonEmpty(request.AddressDetails.AddressLine3),
      county = nonEmpty(request.AddressDetails.AddressLine4),
      postcode = nonEmpty(request.AddressDetails.PostalCode),
      country = nonEmpty(request.AddressDetails.CountryCode),
      uprn = request.AddressDetails.Uprn.map(_.toString),
      primaryContactName = request.PrimaryContactDetails.flatMap(
        contact => nonEmpty(contact.ContactName)
      ),
      primaryContactEmail = request.PrimaryContactDetails.flatMap(
        contact => nonEmpty(contact.EmailAddress)
      ),
      primaryContactTelephone = request.PrimaryContactDetails.flatMap(
        contact => nonEmpty(contact.PhoneNumber)
      ),
      secondaryContactName = request.SecondaryContactDetails.flatMap(
        contact => nonEmpty(contact.ContactName)
      ),
      secondaryContactEmail = request.SecondaryContactDetails.flatMap(
        contact => nonEmpty(contact.EmailAddress)
      ),
      secondaryContactTelephone = request.SecondaryContactDetails.flatMap(
        contact => nonEmpty(contact.PhoneNumber)
      )
    )

    send(
      auditType = "AmendFinancialInstitution",
      event = event
    )
  }

  def sendRemoveFinancialInstitution(
    financialInstitutionId: String,
    fatcaId: String
  )(implicit hc: HeaderCarrier): Unit = {

    val event = RemoveFinancialInstitution(
      financialInstitutionId = financialInstitutionId,
      fatcaId = fatcaId
    )

    send(
      auditType = "RemoveFinancialInstitution",
      event = event
    )
  }

  private def tinValue(
    request: RequestDetails,
    tinType: TINType
  ): Option[String] =
    request.TINDetails.collectFirst {
      case tin if tin.TINType == tinType =>
        tin.TIN
    }

  private def nonEmpty(value: String): Option[String] =
    Option(value).filter(_.trim.nonEmpty)

  private def nonEmpty(value: Option[String]): Option[String] =
    value.filter(_.trim.nonEmpty)

  private def send[E <: AuditEvent](
    auditType: String,
    event: E
  )(implicit
    hc: HeaderCarrier,
    writes: OWrites[E]
  ): Unit = {
    logger.info(s"Auditing $auditType")

    auditConnector.sendExplicitAudit(
      auditType = auditType,
      detail = event
    )
  }

}
