package app.scenario

import io.gatling.core.Predef._ // scalastyle:ignore
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._ // scalastyle:ignore

import scala.util.Random

class CreateOrderScenario {

  private def getRandomItems(): Int = Random.nextInt(4)

  private def getRandomNumber(): Int = Random.nextInt(1000)

  private def createItem(i: Int) = s"""{"quantity":${getRandomNumber()},"product":{"id":"PRD000000${i}"}}"""

  def getItems(): String = {
    val stringBuilder = StringBuilder.newBuilder
    for (i <- 0 to getRandomItems()) {
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
      .exec(
        http("Create a new customer")
          .post("/")
          .body(StringBody(s"""{"firstname":"Test","lastname":"User"}"""))
          .asJson
          .check(jsonPath("$.id").exists.saveAs("customerId"))
      )
      .pause(2)
      .exec(
        http("Read the created customer")
          .get("/${customerId}")
      )
      .pause(10)
      .exec(
        http("Create a new order")
          .post("/${customerId}/orders")
          .body(StringBody(s"""{"items":[${getItems()}]}"""))
          .asJson
          .check(jsonPath("$.id").exists.saveAs("orderId"))
      )
      .pause(2)
      .exec(
        http("Read the created order")
          .get("/${customerId}/orders/${orderId}")
      )
}
