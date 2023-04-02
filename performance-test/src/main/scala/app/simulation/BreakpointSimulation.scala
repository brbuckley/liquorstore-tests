package app.simulation

import app.configuration.ConfigurationLoader.configs
import app.execution.BreakpointExecution
import io.gatling.core.structure.PopulationBuilder

import scala.language.postfixOps

/** Default Breakpoint simulation */
class BreakpointSimulation extends DefaultSimulation {

  val execution: PopulationBuilder = new BreakpointExecution(
    scenario.scn,
    configs.distributed,
    configs.breakpoint
  ).injectExecutions()

  defaultSetup(execution)
}
