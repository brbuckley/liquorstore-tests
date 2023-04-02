package app.configuration

import ConfigurationLoader.find
import com.fasterxml.jackson.annotation.JsonProperty

import java.util.Optional

/**  Get configs from yaml properties or CLI args
  *
  * @param _multiplier Multiplies the base value
  * @param _value Base value
  */
class ThresholdConfig(
  @JsonProperty("multiplier") _multiplier: String,
  @JsonProperty("value") _value: Integer
) {

  val multiplier: Double = find("threshold.multiplier", _multiplier).toDouble
  val value: Int         = find("threshold.value", _value)

}
