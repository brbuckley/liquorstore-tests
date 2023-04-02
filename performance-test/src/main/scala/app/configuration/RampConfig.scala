package app.configuration

import ConfigurationLoader.find
import com.fasterxml.jackson.annotation.JsonProperty

/** Get configs from yaml properties or CLI args
  *
  * @param _down Ramp-down time
  * @param _up Ramp-up time
  */
class RampConfig(@JsonProperty("down") _down: Integer, @JsonProperty("up") _up: Integer) {

  val down: Int = find("ramp.down", _down)
  val up: Int   = find("ramp.up", _up)
}
