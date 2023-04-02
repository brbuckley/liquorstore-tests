package app.scenario

import app.configuration.RequestConfig
import io.gatling.core.Predef._ // scalastyle:ignore
import io.gatling.core.session.Session
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._ // scalastyle:ignore
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.util.Random

class QueryProductsScenario(timeout: Int, request: RequestConfig) {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(request.url)
    .headers(request.headers)

  val scn: ScenarioBuilder =
    scenario("Customers ordering products")
      .exec( (session: Session) => session.set("supplierId","SUP000000" + (Random.nextInt(3) + 1).toString))
      // 30% creates a new product first
      .randomSwitch(
        30.0 -> exec(
          http("Create a new product")
            .post("/${supplierId}")
            .body(StringBody(s"""{"name":"Lorem Ipsum","price":1234569,"category":"beer"}""".stripMargin))
            .asJson
            .check(jsonPath("$.id").exists.saveAs("productId"))
            .check(status.is(201))
            .check(responseTimeInMillis.lte(timeout))
        ).exitHereIfFailed,
        70.0 -> exec( (session: Session) => session.set("productId","PRD0000001"))
      )
      // 40% check all products while 60% check only one product by its ID
      .randomSwitch(
        40.0 -> exec(
          http("Read all products")
            .get("/")
            .check(status.is(200))
            .check(responseTimeInMillis.lte(timeout))
        ),
        60.0 -> exec(
          http("Read product by id")
            .get("/?ids=${productId}")
            .check(status.is(200))
            .check(responseTimeInMillis.lte(timeout))
        )
      )
      // 10% update the product they just added
      .randomSwitch(
        10.0 -> exec(
          http("Read all products")
            .put("/${productId}")
            .body(StringBody(s"""{"name":"Lorem Ipsum","price":10,"category":"wine"}""".stripMargin))
            .asJson
            .check(status.is(200))
            .check(responseTimeInMillis.lte(timeout))
        ),
        90.0 -> pause(0)
      )
}
