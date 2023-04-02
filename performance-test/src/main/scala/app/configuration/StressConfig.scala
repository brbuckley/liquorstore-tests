package app.configuration

import ConfigurationLoader.find
import com.fasterxml.jackson.annotation.JsonProperty

/** Get configs from yaml properties or CLI args
  *
  * @param _duration Maximum duration
  */
class StressConfig(@JsonProperty("duration") _duration: Integer) {

  val duration: Integer = find("stress.duration", _duration)
}
