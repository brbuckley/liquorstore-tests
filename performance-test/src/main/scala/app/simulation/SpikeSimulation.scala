package app.simulation

import app.execution.SpikeExecution
import io.gatling.core.structure.PopulationBuilder

/** Default Spike simulation */
class SpikeSimulation extends DefaultSimulation {

  val execution: PopulationBuilder = new SpikeExecution(
    scenario.scn,
    default.conf.distributed,
    default.conf.threshold,
    default.conf.spike
  ).injectExecutions()

  defaultSetup(execution)
}
