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

import org.scalacheck.Arbitrary._
import org.scalacheck.{Gen, Shrink}
import wolfendale.scalacheck.regexp.RegexpGen

trait Generators extends ModelGenerators {

  implicit val dontShrink: Shrink[String] = Shrink.shrinkAny

  val subscriptionIDRegex = "^[X][A-Z][0-9]{13}"
  val phoneNumberRegex    = "[A-Z0-9)/(\\-*#+]*"

  def validSubscriptionID: Gen[String] = RegexpGen.from(subscriptionIDRegex)
  def validPhoneNumber: Gen[String]    = RegexpGen.from(phoneNumberRegex)

  def stringOfLength(n: Int): Gen[String] = Gen.listOfN(n, Gen.alphaChar).map(_.mkString)

}
