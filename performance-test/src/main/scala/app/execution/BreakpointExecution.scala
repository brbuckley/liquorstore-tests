package app.execution

import app.configuration.BreakpointConfig
import app.util.CalculationsUtil.exponentialCalculation
import io.gatling.core.Predef._ // scalastyle:ignore
import io.gatling.core.session.Session
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import io.gatling.commons.stats.KO

/** Breakpoint execution used by all breakpoint simulations
  *
  * @param scn Scenario
  * @param distributed Number of machines in distributed mode
  * @param breakpointConfig Breakpoint Specific Configuration
  */
class BreakpointExecution(
  scn: ScenarioBuilder,
  distributed: Integer,
  breakpointConfig: BreakpointConfig
) {

  var requestFail: Int = 0
  val margin: Int      = 100

  def injectExecutions(): PopulationBuilder =
    scn
      .exec(
        (session: Session) => {
          // Checks for tolerance when request fails
          if (session.status == KO) {
            requestFail += 1
            if (requestFail >= breakpointConfig.tolerance) {
              java.lang.System.exit(0)
            }
          }
          session
        }
      )
      .inject(
        // Reference function: f(t) = t + A^(t+B)
        (0 to margin + breakpointConfig.duration by +margin).flatMap(
          i =>
            Seq(
              rampUsersPerSec(
                exponentialCalculation(
                  i,
                  breakpointConfig.function.inaccuracy,
                  breakpointConfig.function.initial_load,
                  distributed.toDouble
                )
              ).to(
                  exponentialCalculation(
                    i + margin,
                    breakpointConfig.function.inaccuracy,
                    breakpointConfig.function.initial_load,
                    distributed.toDouble
                  )
                )
                .during(margin)
            )
        )
      )
}
