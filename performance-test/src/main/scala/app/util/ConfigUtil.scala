package app.util

import java.util.Optional

/** Utility class for configuration */
object ConfigUtil {

  def optionalEnvYaml[T](path: String, default: T): T =
    default match {
      case option: String =>
        Optional.ofNullable(System.getProperty(path)).filter(s => s.nonEmpty).orElse(option).asInstanceOf[T]
      case option: Integer => Optional.ofNullable(Integer.getInteger(path)).orElse(option).asInstanceOf[T]
    }
}
