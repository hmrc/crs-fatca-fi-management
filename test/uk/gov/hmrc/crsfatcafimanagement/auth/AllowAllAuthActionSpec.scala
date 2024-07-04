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

package uk.gov.hmrc.crsfatcafimanagement.auth

import org.apache.pekko.util.Timeout
import org.mockito.ArgumentMatchers.any
import play.api.Application
import play.api.http.Status.{OK, UNAUTHORIZED}
import play.api.inject.bind
import play.api.mvc.{Action, AnyContent, InjectedController}
import play.api.test.FakeRequest
import play.api.test.Helpers.status
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.crsfatcafimanagement.SpecBase
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

class AllowAllAuthActionSpec extends SpecBase {

  class Harness(authAction: AllowAllAuthAction) extends InjectedController {

    def onPageLoad(): Action[AnyContent] = authAction {
      _ => Ok
    }

  }

  val mockAuthConnector: AuthConnector = mock[AuthConnector]

  implicit val timeout: Timeout = 5 seconds

  override lazy val app: Application = applicationBuilder()
    .overrides(bind[AuthConnector].toInstance(mockAuthConnector))
    .build()

  "Allow All Auth Action" - {
    "when the user is not logged in" - {
      "must return UNAUTHORIZED" in {

        when(
          mockAuthConnector.authorise(any[Predicate](), any[Retrieval[_]]())(
            any[HeaderCarrier](),
            any[ExecutionContext]()
          )
        ).thenReturn(Future.failed(new MissingBearerToken))

        val authAction = app.injector.instanceOf[AllowAllAuthAction]
        val controller = new Harness(authAction)

        val result = controller.onPageLoad()(FakeRequest("", ""))

        status(result) mustBe UNAUTHORIZED
      }
    }

    "when the user is logged in" - {
      "must return OK" in {
        when(
          mockAuthConnector
            .authorise[Unit](
              any[Predicate](),
              any[Retrieval[Unit]]()
            )(any[HeaderCarrier](), any[ExecutionContext]())
        ) thenReturn Future.successful(())

        val authAction = app.injector.instanceOf[AllowAllAuthAction]
        val controller = new Harness(authAction)

        val result = controller.onPageLoad()(FakeRequest("", ""))

        status(result) mustBe OK
      }
    }
  }

}
