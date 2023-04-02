package app.scenario

import app.configuration.RequestConfig
import io.gatling.core.Predef._ // scalastyle:ignore
import io.gatling.core.session.Session
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._ // scalastyle:ignore
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.util.Random

class CreateOrderScenario(timeout: Int, request: RequestConfig) {

  private def getRandomItems: Int = Random.nextInt(4)

  private def getRandomNumber: Int = Random.nextInt(1000)

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(request.url)
    .headers(request.headers)

  private def createItem(i: Int) = s"""{"quantity":${getRandomNumber},"product":{"id":"PRD000000${i}"}}"""

  def getItems(): String = {
    val stringBuilder = StringBuilder.newBuilder
    for (i <- 0 to getRandomItems) {
      if (i == 0) {
        stringBuilder.append(createItem(i + 1))
      } else {
        stringBuilder.append(",").append(createItem(i + 1))
      }
    }
    stringBuilder.toString()
  }

  val scn: ScenarioBuilder =
    scenario("Customers ordering products")
    // 20% are new customers
      .randomSwitch(
        20.0 -> exec(
          http("Create a new customer")
            .post("/")
            .body(StringBody(s"""{"firstname":"Test","lastname":"User"}"""))
            .asJson
            .check(jsonPath("$.id").exists.saveAs("customerId"))
            .check(status.is(201))
            .check(responseTimeInMillis.lte(timeout))
        ).exitHereIfFailed,
        80.0 -> exec((session: Session) => session.set("customerId", "CST0000001"))
      )
      // 25% check their profile before ordering
      .randomSwitch(
        25.0 -> exec(
          http("Read the created customer")
            .get("/${customerId}")
            .check(status.is(200))
            .check(responseTimeInMillis.lte(timeout))
        ),
        75.0 -> pause(0)
      )
      // This is the required step of our scenario
      .exec(
        http("Create a new order")
          .post("/${customerId}/orders")
          .body(StringBody(s"""{"items":[${getItems()}]}"""))
          .asJson
          .check(jsonPath("$.id").exists.saveAs("orderId"))
          .check(status.is(201))
          .check(responseTimeInMillis.lte(timeout))
      )
      .exitHereIfFailed
      // 50% check the status of the order shortly after
      .randomSwitch(
        50.0 -> pause(10).exec(
          http("Read the created order")
            .get("/${customerId}/orders/${orderId}")
            .check(status.is(200))
            .check(responseTimeInMillis.lte(timeout))
        ),
        50.0 -> pause(0)
      )
}
