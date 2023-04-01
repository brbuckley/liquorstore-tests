package app.scenario

import app.configuration.RequestConfig
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.netty.handler.codec.http.HttpMethod

/** Default scenario used as base for test specific scenarios
  *
  * @param timeout Request timeout
  * @param request Request configurations
  */
class DefaultScenario(timeout: Int, request: RequestConfig) {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(request.url)
    .headers(request.headers)

  var scn: ScenarioBuilder = scenario(request.http_method)
  scn = scn
    .exec(
      http("Call API")
        .httpRequest(HttpMethod.valueOf(request.http_method), "")
        .check(status.is(request.expected_status))
        .check(responseTimeInMillis.lte(timeout))
    )

}
