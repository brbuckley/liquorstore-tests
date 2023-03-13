package app.scenario

import app.configuration.RequestConfig
import io.gatling.core.Predef._ // scalastyle:ignore
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._ // scalastyle:ignore
import io.gatling.http.protocol.HttpProtocolBuilder

/** Default scenario used as base for test specific scenarios
  *
  * @param timeout Request timeout
  * @param request Request configurations
  */
class DefaultScenario(timeout: Int, request: RequestConfig) {

  val DEFAULT_ENDPOINT: String = "/"
  val DEFAULT_NAME: String     = "Call API"
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(request.url)
    .headers(request.headers)

  val scn: ScenarioBuilder = request.http_method match {
    case "GET" =>
      scenario("GET")
        .exec(
          http("Call API")
            .get("/")
            .check(status.is(request.expected_status))
            .check(responseTimeInMillis.lte(timeout))
        )
    case "POST" =>
      scenario("POST")
        .exec(
          http(DEFAULT_NAME)
            .post(DEFAULT_ENDPOINT)
            .body(StringBody(request.body))
            .check(status.is(request.expected_status))
            .check(responseTimeInMillis.lte(timeout))
        )
    case "PUT" =>
      scenario("PUT")
        .exec(
          http(DEFAULT_NAME)
            .put(DEFAULT_ENDPOINT)
            .body(StringBody(request.body))
            .check(status.is(request.expected_status))
            .check(responseTimeInMillis.lte(timeout))
        )
    case "PATCH" =>
      scenario("PATCH")
        .exec(
          http(DEFAULT_NAME)
            .patch(DEFAULT_ENDPOINT)
            .body(StringBody(request.body))
            .check(status.is(request.expected_status))
            .check(responseTimeInMillis.lte(timeout))
        )
    case "DELETE" =>
      scenario("DELETE")
        .exec(
          http(DEFAULT_NAME)
            .delete(DEFAULT_ENDPOINT)
            .body(StringBody(request.body))
            .check(status.is(request.expected_status))
            .check(responseTimeInMillis.lte(timeout))
        )
    case _ => throw new Exception("Invalid http method")
  }
}
