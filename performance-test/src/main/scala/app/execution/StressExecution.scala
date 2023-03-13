package app.execution

import app.configuration.{RampConfig, ThresholdConfig}
import io.gatling.core.Predef._    // scalastyle:ignore
import io.gatling.core.structure._ // scalastyle:ignore

import java.util.Optional
import scala.concurrent.duration._ // scalastyle:ignore
import scala.language.postfixOps

/** Stress execution used by all stress simulations
  *
  * @param scn Scenario
  * @param distributed Number of machines in distributed mode
  * @param ramp Ramp up and down time
  * @param threshold API threshold
  * @param duration Maximum duration
  */
class StressExecution(
  scn: ScenarioBuilder,
  distributed: Int,
  ramp: RampConfig,
  threshold: ThresholdConfig,
  duration: Int
) {

  val multiplier: Double     = Optional.of(threshold.multiplier).filter(d => d != 0).orElse(1.2)
  val requestsPerSecond: Int = ((threshold.value * multiplier) / distributed).toInt

  def injectExecutions(): PopulationBuilder =
    scn
      .inject(
        rampUsersPerSec(1) to requestsPerSecond during (ramp.up seconds),
        constantUsersPerSec(requestsPerSecond) during (duration seconds),
        rampUsersPerSec(requestsPerSecond) to 1 during (ramp.down seconds)
      )
}
