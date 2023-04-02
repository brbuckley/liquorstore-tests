package app.scenario

import app.configuration.RequestConfig
import io.gatling.core.Predef._ // scalastyle:ignore
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._ // scalastyle:ignore
import io.gatling.http.protocol.HttpProtocolBuilder

class QueryPurchasesScenario(timeout: Int, request: RequestConfig) {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(request.url)
    .headers(request.headers)

  val scn: ScenarioBuilder =
    scenario("Customers ordering products")
    // 90% searches by order id while 10% search by purchase id
      .randomSwitch(
        90.0 -> exec(
          http("Search by Order id")
            .get("/?order-id=ORD0000001")
            .check(status.is(200))
            .check(responseTimeInMillis.lte(timeout))
        ),
        10.0 -> exec(
          http("Search by Purchase id")
            .get("/PUR0000001")
            .check(status.is(200))
            .check(responseTimeInMillis.lte(timeout))
        )
      )

}
