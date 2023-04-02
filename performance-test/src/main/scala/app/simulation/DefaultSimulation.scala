package app.simulation

import app.configuration.ConfigurationLoader.configs
import app.scenario.{CreateOrderScenario}
import io.gatling.core.Predef.{Simulation, configuration, global}
import io.gatling.core.structure.PopulationBuilder

abstract class DefaultSimulation extends Simulation {

  val scenario               = new CreateOrderScenario(configs.timeout, configs.request)

  def defaultSetup(execution: PopulationBuilder): SetUp =
    setUp(
      execution
    ).protocols(scenario.httpProtocol)
      .assertions(
        global.responseTime.max.lt(configs.timeout),
        global.successfulRequests.percent.gte(configs.success_rate.toInt)
      )

}
