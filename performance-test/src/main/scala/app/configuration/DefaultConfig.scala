package app.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import app.util.CalculationsUtil.exponentialCalculation
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

import java.io.FileReader

/** Builds and validates the config objects */
class DefaultConfig {

  val DEFAULT_DURATION = 100
  val mapper           = new ObjectMapper(new YAMLFactory())
  val reader           = new FileReader(getClass.getResource("/application.yaml").getPath)
  val conf: Configs    = mapper.readValue(reader, classOf[Configs])

  // Check for Double infinity
  if (exponentialCalculation(
        conf.breakpoint.duration + 2 * DEFAULT_DURATION,
        conf.breakpoint.function.inaccuracy,
        conf.breakpoint.function.initial_load,
        conf.distributed.toDouble
      ).isInfinite) {
    throw new Exception("Breakpoint function causes Double infinity")
  }

}
