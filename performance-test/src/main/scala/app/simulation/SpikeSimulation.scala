package app.simulation

import app.configuration.ConfigurationLoader.configs
import app.execution.SpikeExecution
import io.gatling.core.structure.PopulationBuilder

/** Default Spike simulation */
class SpikeSimulation extends DefaultSimulation {

  val execution: PopulationBuilder = new SpikeExecution(
    scenario.scn,
    configs.distributed,
    configs.threshold,
    configs.spike
  ).injectExecutions()

  defaultSetup(execution)
}
