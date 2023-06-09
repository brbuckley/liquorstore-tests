package app.configuration

import ConfigurationLoader.find
import com.fasterxml.jackson.annotation.JsonProperty

/**  Get configs from yaml properties or CLI args
  *
  * @param _body Request Body
  * @param _expected_status Expected http status from response
  * @param _headers Request headers
  * @param _http_method Request http method
  * @param _url Request url
  */
class RequestConfig(
  @JsonProperty("body") _body: String,
  @JsonProperty("expected_status") _expected_status: Integer,
  @JsonProperty("headers") _headers: String,
  @JsonProperty("http_method") _http_method: String,
  @JsonProperty("url") _url: String
) {

  val body: String         = find("request.body", _body)
  val expected_status: Int = find("request.expected_status", _expected_status)
  val headers: Map[String, String] = find("request.headers", _headers)
    .replaceAll("['\n\"{}\\\\]", "")
    .split(" *, *")
    .map(_.split(" *: *"))
    .map { case Array(k, v) => (k, v) }
    .toMap
  val http_method: String =
    find("request.http_method", _http_method)
  val url: String = find("request.url", _url)
}
