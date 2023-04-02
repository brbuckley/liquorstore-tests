package app.simulation

import app.configuration.ConfigurationLoader.configs
import app.execution.StressExecution
import io.gatling.core.structure.PopulationBuilder

import scala.language.postfixOps

/** Default Stress simulation */
class StressSimulation extends DefaultSimulation {

  val execution: PopulationBuilder =
    new StressExecution(
      scenario.scn,
      configs.distributed,
      configs.ramp,
      configs.threshold,
      configs.stress.duration
    ).injectExecutions()

  defaultSetup(execution)
}
