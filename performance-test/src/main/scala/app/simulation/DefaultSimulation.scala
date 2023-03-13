package app.simulation

import app.configuration.DefaultConfig
import app.scenario.DefaultScenario
import io.gatling.core.Predef.{Simulation, configuration, global}
import io.gatling.core.structure.PopulationBuilder

abstract class DefaultSimulation extends Simulation {

  val default: DefaultConfig = new DefaultConfig()
  val scenario               = new DefaultScenario(default.conf.timeout, default.conf.request)

  def defaultSetup(execution: PopulationBuilder): SetUp =
    setUp(
      execution
    ).protocols(scenario.httpProtocol)
      .assertions(
        global.responseTime.max.lt(default.conf.timeout),
        global.successfulRequests.percent.gte(default.conf.success_rate.toInt)
      )

}
