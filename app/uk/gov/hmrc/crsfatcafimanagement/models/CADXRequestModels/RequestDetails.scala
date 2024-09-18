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

package uk.gov.hmrc.crsfatcafimanagement.models.CADXRequestModels

import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.crsfatcafimanagement.models.{AddressDetails, ContactDetails, TINDetails}

final case class CreateRequestDetails(
  FIName: String,
  SubscriptionID: String,
  TINDetails: List[TINDetails],
  IsFIUser: Boolean,
  IsFATCAReporting: Boolean,
  AddressDetails: AddressDetails,
  PrimaryContactDetails: Option[ContactDetails],
  SecondaryContactDetails: Option[ContactDetails]
)

object CreateRequestDetails {
  implicit val format: OFormat[CreateRequestDetails] = Json.format[CreateRequestDetails]
}

final case class RemoveRequestDetails(
  SubscriptionID: String,
  FIID: String,
  FIName: Option[String] = None,
  TINDetails: Option[List[TINDetails]] = None,
  IsFIUser: Option[Boolean] = None,
  IsFATCAReporting: Option[Boolean] = None,
  PrimaryContactDetails: Option[ContactDetails] = None,
  SecondaryContactDetails: Option[ContactDetails] = None,
  AddressDetails: Option[AddressDetails] = None
)

object RemoveRequestDetails {
  implicit val format: OFormat[RemoveRequestDetails] = Json.format[RemoveRequestDetails]
}
