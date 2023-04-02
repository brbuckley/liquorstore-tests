package app.configuration

import ConfigurationLoader.find
import com.fasterxml.jackson.annotation.JsonProperty

/** Get configs from yaml properties or CLI args
  *
  * @param _duration Maximum test duration
  * @param _tolerance Request fail tolerance
  * @param _function Exponential function configs
  */
class BreakpointConfig(
  @JsonProperty("duration") _duration: Integer,
  @JsonProperty("tolerance") _tolerance: Integer,
  @JsonProperty("function") _function: FunctionConfig
) {

  val duration: Integer        = find("breakpoint.duration", _duration)
  val tolerance: Integer       = find("breakpoint.tolerance", _tolerance)
  val function: FunctionConfig = _function
}
