package app.simulation

import app.execution.StressExecution
import io.gatling.core.structure.PopulationBuilder

import scala.language.postfixOps

/** Default Stress simulation */
class StressSimulation extends DefaultSimulation {

  val execution: PopulationBuilder =
    new StressExecution(
      scenario.scn,
      default.conf.distributed,
      default.conf.ramp,
      default.conf.threshold,
      default.conf.stress.duration
    ).injectExecutions()

  defaultSetup(execution)
}
