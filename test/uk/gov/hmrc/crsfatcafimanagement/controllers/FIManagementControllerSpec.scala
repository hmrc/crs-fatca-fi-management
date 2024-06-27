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

package uk.gov.hmrc.crsfatcafimanagement.controllers

import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.inject.bind
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.crsfatcafimanagement.SpecBase
import uk.gov.hmrc.crsfatcafimanagement.auth.{AllowAllAuthAction, FakeAllowAllAuthAction}
import uk.gov.hmrc.crsfatcafimanagement.connectors.CADXConnector
import uk.gov.hmrc.crsfatcafimanagement.generators.Generators

class FIManagementControllerSpec extends SpecBase with Generators with ScalaCheckPropertyChecks {

  val mockAuthConnector: AuthConnector = mock[AuthConnector]

  val mockCADXConnector: CADXConnector = mock[CADXConnector]

  val application: Application = applicationBuilder()
    .overrides(
      bind[CADXConnector].toInstance(mockCADXConnector),
      bind[AuthConnector].toInstance(mockAuthConnector),
      bind[AllowAllAuthAction].to[FakeAllowAllAuthAction]
    )
    .build()

}
