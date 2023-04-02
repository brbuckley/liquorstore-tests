package app.configuration

import ConfigurationLoader.find
import com.fasterxml.jackson.annotation.JsonProperty

/** Get configs from yaml properties or CLI args
  *
  * @param _period Duration of each spike
  * @param _repetition Number of spike repetitions
  * @param _wait_time Duration of wait time between spikes
  */
class SpikeConfig(
  @JsonProperty("period") _period: Integer,
  @JsonProperty("repetition") _repetition: Integer,
  @JsonProperty("wait_time") _wait_time: Integer
) {

  val period: Int         = find("spike.period", _period)
  val repetition: Integer = find("spike.repetition", _repetition)
  val wait_time: Integer  = find("spike.wait_time", _wait_time)
}
