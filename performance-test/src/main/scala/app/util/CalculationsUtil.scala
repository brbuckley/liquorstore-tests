package app.util

/** Utility class for repeated calculations along the code */
object CalculationsUtil {
  def exponentialCalculation(time: Double, exponent: Double, initial_load: Double, distributed: Double): Double =
    (time + Math.pow(exponent, time + Math.log(initial_load) / Math.log(exponent))) / distributed
}
