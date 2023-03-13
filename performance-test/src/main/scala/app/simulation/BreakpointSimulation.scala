package app.simulation

import app.execution.BreakpointExecution
import io.gatling.core.structure.PopulationBuilder

import scala.language.postfixOps

/** Default Breakpoint simulation */
class BreakpointSimulation extends DefaultSimulation {

  val execution: PopulationBuilder = new BreakpointExecution(
    scenario.scn,
    default.conf.distributed,
    default.conf.breakpoint
  ).injectExecutions()

  defaultSetup(execution)
}
