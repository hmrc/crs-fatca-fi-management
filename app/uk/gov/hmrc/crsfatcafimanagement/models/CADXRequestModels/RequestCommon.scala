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

import play.api.libs.json.JsonConfiguration.Aux
import play.api.libs.json.{Json, JsonConfiguration, JsonNaming, OFormat}
import uk.gov.hmrc.crsfatcafimanagement.models.RequestType

final case class RequestCommon(
  originatingSystem: String,
  transmittingSystem: String,
  requestType: RequestType,
  regime: String,
  requestParameters: List[RequestParameter]
)

object RequestCommon {
  implicit val jsonConfig: Aux[Json.MacroOptions] = JsonConfiguration(naming = JsonNaming.PascalCase)
  implicit val format: OFormat[RequestCommon]     = Json.format[RequestCommon]
}
