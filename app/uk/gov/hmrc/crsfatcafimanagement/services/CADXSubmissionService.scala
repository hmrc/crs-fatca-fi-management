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

package uk.gov.hmrc.crsfatcafimanagement.services

import play.api.Logging
import play.api.http.Status.OK
import uk.gov.hmrc.crsfatcafimanagement.connectors.CADXConnector
import uk.gov.hmrc.crsfatcafimanagement.models.CADXRequestModels.CreateFIDetailsRequest
import uk.gov.hmrc.crsfatcafimanagement.models.errors.CreateSubmissionError
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class CADXSubmissionService @Inject() (connector: CADXConnector) extends Logging {

  def createFI(
    createFIDetails: CreateFIDetailsRequest
  )(implicit hc: HeaderCarrier, ex: ExecutionContext): Future[Either[CreateSubmissionError, Unit]] =
    connector.createFI(createFIDetails).map {
      res =>
        res.status match {
          case OK => Right(())
          case status =>
            logger.warn(s"create submission Got Status $status")
            Left(CreateSubmissionError(status))
        }
    }

}
