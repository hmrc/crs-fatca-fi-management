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

sealed trait RequestDetails {
  val SubscriptionID: String
  val TINDetails: List[TINDetails]
  val IsFIUser: Boolean
  val IsFATCAReporting: Boolean
  val AddressDetails: AddressDetails
  val PrimaryContactDetails: Option[ContactDetails]   = None
  val SecondaryContactDetails: Option[ContactDetails] = None
}

object RequestDetails {
  implicit val format: OFormat[RequestDetails] = Json.format[RequestDetails]
}

final case class CreateRequestDetails(
  FIName: String,
  SubscriptionID: String,
  TINDetails: List[TINDetails],
  IsFIUser: Boolean,
  IsFATCAReporting: Boolean,
  AddressDetails: AddressDetails,
  override val PrimaryContactDetails: Option[ContactDetails] = None,
  override val SecondaryContactDetails: Option[ContactDetails] = None
) extends RequestDetails

object CreateRequestDetails {
  implicit val format: OFormat[CreateRequestDetails] = Json.format[CreateRequestDetails]
}

final case class UpdateRequestDetails(
  FIID: String,
  FIName: String,
  SubscriptionID: String,
  TINDetails: List[TINDetails],
  IsFIUser: Boolean,
  IsFATCAReporting: Boolean,
  AddressDetails: AddressDetails,
  override val PrimaryContactDetails: Option[ContactDetails] = None,
  override val SecondaryContactDetails: Option[ContactDetails] = None
) extends RequestDetails

object UpdateRequestDetails {
  implicit val format: OFormat[UpdateRequestDetails] = Json.format[UpdateRequestDetails]
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
