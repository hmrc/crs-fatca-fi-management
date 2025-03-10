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

import play.api.libs.json.{Format, Json, OFormat}

final case class FIManagement[R](FIManagement: R)

object FIManagement {
  implicit def FIManagementFormat[R: Format]: OFormat[FIManagement[R]] = Json.format[FIManagement[R]]
}

final case class FIDetailsRequest[T <: RequestDetails](RequestCommon: RequestCommon, RequestDetails: T)

object FIDetailsRequest {
  implicit def format[T <: RequestDetails: OFormat]: OFormat[FIDetailsRequest[T]] = Json.format[FIDetailsRequest[T]]
}

final case class RemoveFIDetailsRequest(RequestCommon: RequestCommon, RequestDetails: RemoveRequestDetails)

object RemoveFIDetailsRequest {
  implicit val format: OFormat[RemoveFIDetailsRequest] = Json.format[RemoveFIDetailsRequest]
}
