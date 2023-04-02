package app.configuration

import ConfigurationLoader.find
import com.fasterxml.jackson.annotation.JsonProperty

/** Get configs from yaml properties or CLI args
  *
  * @param _inaccuracy Abstract value of inaccuracy o the function
  * @param _initial_load Initial load of Requests/sec
  */
class FunctionConfig(
  @JsonProperty("inaccuracy") _inaccuracy: String,
  @JsonProperty("initial_load") _initial_load: String
) {

  val inaccuracy: Double   = ("1." + find("breakpoint.function.inaccuracy", _inaccuracy)).toDouble
  val initial_load: Double = find("breakpoint.function.initial_load", _initial_load).toDouble
}
