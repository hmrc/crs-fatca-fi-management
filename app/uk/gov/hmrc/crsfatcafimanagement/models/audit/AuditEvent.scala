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

package uk.gov.hmrc.crsfatcafimanagement.models.audit

import play.api.libs.json.{Json, OFormat}

sealed trait AuditEvent

final case class RemoveFinancialInstitution(
  financialInstitutionId: String,
  fatcaId: String
) extends AuditEvent

object RemoveFinancialInstitution {

  implicit val format: OFormat[RemoveFinancialInstitution] =
    Json.format[RemoveFinancialInstitution]

}

final case class AddFinancialInstitution(
  fatcaId: String,
  isRegisteredBusiness: Boolean,
  financialInstitutionId: String,
  financialInstitutionName: String,
  utr: Option[String],
  crn: Option[String],
  urn: Option[String],
  giin: Option[String],
  addressLine1: String,
  addressLine2: Option[String],
  city: Option[String],
  county: Option[String],
  postcode: Option[String],
  country: Option[String],
  uprn: Option[String],
  primaryContactName: Option[String],
  primaryContactEmail: Option[String],
  primaryContactTelephone: Option[String],
  secondaryContactName: Option[String],
  secondaryContactEmail: Option[String],
  secondaryContactTelephone: Option[String]
) extends AuditEvent

object AddFinancialInstitution {

  implicit val format: OFormat[AddFinancialInstitution] =
    Json.format[AddFinancialInstitution]

}
