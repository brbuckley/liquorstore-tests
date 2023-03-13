package app.configuration

import app.util.ConfigUtil.optionalEnvYaml
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

  val period: Int         = optionalEnvYaml("spike.period", _period)
  val repetition: Integer = optionalEnvYaml("spike.repetition", _repetition)
  val wait_time: Integer  = optionalEnvYaml("spike.wait_time", _wait_time)
}
