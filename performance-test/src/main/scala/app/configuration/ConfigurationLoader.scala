package app.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import app.util.CalculationsUtil.exponentialCalculation
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

import java.io.FileReader
import java.util.Optional

/** Builds and validates the config objects */
object ConfigurationLoader {

  val DEFAULT_DURATION = 100
  val mapper           = new ObjectMapper(new YAMLFactory())
  val reader           = new FileReader(getClass.getResource("/application.yaml").getPath)
  val configs: Configs    = mapper.readValue(reader, classOf[Configs])

  // Check for Double infinity
  if (exponentialCalculation(
        configs.breakpoint.duration + 2 * DEFAULT_DURATION,
        configs.breakpoint.function.inaccuracy,
        configs.breakpoint.function.initial_load,
        configs.distributed.toDouble
      ).isInfinite) {
    throw new Exception("Breakpoint function causes Double infinity")
  }

  def find[T](path: String, default: T): T =
    default match {
      case option: String =>
        Optional.ofNullable(System.getProperty(path)).filter(s => s.nonEmpty).orElse(option).asInstanceOf[T]
      case option: Integer => Optional.ofNullable(Integer.getInteger(path)).orElse(option).asInstanceOf[T]
    }
}
