package app.execution

import app.configuration.{SpikeConfig, ThresholdConfig}
import io.gatling.core.Predef._    // scalastyle:ignore
import io.gatling.core.structure._ // scalastyle:ignore

import java.util.Optional
import scala.concurrent.duration._ // scalastyle:ignore
import scala.language.postfixOps

/** Spike execution used by all spike simulations
  *
  * @param scn Scenario
  * @param distributed Number of machines in distributed mode
  * @param threshold API threshold
  * @param spikeConfig Spike specific configurations
  */
class SpikeExecution(
  scn: ScenarioBuilder,
  distributed: Int,
  threshold: ThresholdConfig,
  spikeConfig: SpikeConfig
) {

  val multiplier: Double     = Optional.of(threshold.multiplier).filter(d => d != 0).orElse(2)
  val requestsPerSecond: Int = ((threshold.value * multiplier) / distributed).toInt

  def injectExecutions(): PopulationBuilder =
    scn
      .inject(
        (1 to spikeConfig.repetition).flatMap(
          _ =>
            Seq(
              rampUsersPerSec(1) to requestsPerSecond during (spikeConfig.period / 2 seconds),
              rampUsersPerSec(requestsPerSecond) to 1 during (spikeConfig.period / 2 seconds),
              nothingFor(spikeConfig.wait_time seconds)
            )
        )
      )
}
