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

package uk.gov.hmrc.crsfatcafimanagement.models

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json._

class TINTypeSpec extends AnyWordSpec with Matchers {

  "TINType" should {
    "parse every valid TIN string to its corresponding object" in {
      Json.fromJson[TINType](JsString("UTR")) shouldBe JsSuccess(TINType.UTR)
      Json.fromJson[TINType](JsString("CRN")) shouldBe JsSuccess(TINType.CRN)
      Json.fromJson[TINType](JsString("TURN")) shouldBe JsSuccess(TINType.TURN)
      Json.fromJson[TINType](JsString("OTHER")) shouldBe JsSuccess(TINType.OTHER)
    }

    "reject an unknown string with a JsError" in {
      Json.fromJson[TINType](JsString("TEST_TIN")) shouldBe
        JsError("Invalid TINType: TEST_TIN")
    }

    "reject any non-string JSON value" in {
      Json.fromJson[TINType](JsNumber(1)) shouldBe a[JsError]
      Json.fromJson[TINType](JsBoolean(true)) shouldBe a[JsError]
      Json.fromJson[TINType](JsNull) shouldBe a[JsError]
      Json.fromJson[TINType](JsObject.empty) shouldBe a[JsError]
    }

    "serialise every value back to a JSON string of its name" in {
      Json.toJson[TINType](TINType.UTR) shouldBe JsString("UTR")
      Json.toJson[TINType](TINType.CRN) shouldBe JsString("CRN")
      Json.toJson[TINType](TINType.TURN) shouldBe JsString("TURN")
      Json.toJson[TINType](TINType.OTHER) shouldBe JsString("OTHER")
    }

    "have values unchanged when toJson then fromJson" in {
      TINType.allValues.foreach {
        tin =>
          Json.fromJson[TINType](Json.toJson(tin)) shouldBe JsSuccess(tin)
      }
    }
  }

}
