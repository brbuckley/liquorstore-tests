package app.configuration

import app.util.ConfigUtil.optionalEnvYaml
import com.fasterxml.jackson.annotation.JsonProperty

/** Get configs from yaml properties or CLI args
  *
  * @param _distributed Distributed testing
  * @param _success_rate Expected success rate
  * @param _timeout Request timeout
  * @param _request Request specific configs
  * @param _threshold API threshold
  * @param _breakpoint Breakpoint specific configs
  * @param _endurance Endurance specific configs
  * @param _spike Spike specific configs
  * @param _stress Stress specific configs
  */
class Configs(
  @JsonProperty("distributed") _distributed: Integer,
  @JsonProperty("success_rate") _success_rate: Integer,
  @JsonProperty("timeout") _timeout: Integer,
  @JsonProperty("ramp") _ramp: RampConfig,
  @JsonProperty("request") _request: RequestConfig,
  @JsonProperty("threshold") _threshold: ThresholdConfig,
  @JsonProperty("breakpoint") _breakpoint: BreakpointConfig,
  @JsonProperty("spike") _spike: SpikeConfig,
  @JsonProperty("stress") _stress: StressConfig
) {

  val distributed: Integer         = optionalEnvYaml("distributed", _distributed)
  val success_rate: Integer        = optionalEnvYaml("success_rate", _success_rate)
  val timeout: Integer             = optionalEnvYaml("timeout", _timeout)
  val ramp: RampConfig             = _ramp
  val request: RequestConfig       = _request
  val threshold: ThresholdConfig   = _threshold
  val breakpoint: BreakpointConfig = _breakpoint
  val spike: SpikeConfig           = _spike
  val stress: StressConfig         = _stress
}
