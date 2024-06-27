package uk.gov.hmrc.crsfatcafimanagement.connectors

import org.scalatest.concurrent.IntegrationPatience
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import uk.gov.hmrc.crsfatcafimanagement.SpecBase
import uk.gov.hmrc.crsfatcafimanagement.generators.Generators
import uk.gov.hmrc.crsfatcafimanagement.wiremock.WireMockHelper

class CADXConnectorSpec
  extends SpecBase
    with Generators
    with IntegrationPatience
    with WireMockHelper
    with ScalaCheckPropertyChecks {

  override def beforeAll(): Unit = {
    super.beforeAll()
    startWireMock()
  }

  override def afterAll(): Unit = {
    stopWireMock()
    super.afterAll()
  }

}
