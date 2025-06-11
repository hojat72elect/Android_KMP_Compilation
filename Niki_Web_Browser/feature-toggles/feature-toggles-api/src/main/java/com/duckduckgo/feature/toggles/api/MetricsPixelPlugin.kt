

package com.duckduckgo.feature.toggles.api

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Experiment pixels that want to be fired should implement this plugin. The associated plugin point
 * will call the plugins before the pixels that match the [MetricsPixel] spec are fired.
 */
interface MetricsPixelPlugin {
    /**
     * @return a list of [MetricsPixel] that will be intercepted and processed prior firing to adhere to our
     * experimental practises
     */
    suspend fun getMetrics(): List<MetricsPixel>
}

data class ConversionWindow(val lowerWindow: Int, val upperWindow: Int)

data class PixelDefinition(val pixelName: String, val params: Map<String, String>)

data class MetricsPixel(
    val metric: String,
    val value: String,
    val conversionWindow: List<ConversionWindow>,
    val toggle: Toggle,
) {

    fun getPixelDefinitions(): List<PixelDefinition> {
        val cohort = toggle.getRawStoredState()?.assignedCohort?.name.orEmpty()
        val enrollmentDateET = toggle.getRawStoredState()?.assignedCohort?.enrollmentDateET?.let {
            ZonedDateTime.parse(it).truncatedTo(ChronoUnit.DAYS).format(DateTimeFormatter.ISO_LOCAL_DATE)
        }.orEmpty()
        if (cohort.isEmpty() || enrollmentDateET.isEmpty()) {
            return emptyList()
        }

        val pixelName = "${METRICS_PIXEL_PREFIX}_${toggle.featureName().name}_$cohort"

        return conversionWindow.map { window ->
            val params = mutableMapOf<String, String>()
            val conversionWindowDays = if (window.lowerWindow == window.upperWindow) {
                "${window.lowerWindow}"
            } else {
                "${window.lowerWindow}-${window.upperWindow}"
            }

            params["metric"] = metric
            params["value"] = value
            params["enrollmentDate"] = enrollmentDateET
            params["conversionWindowDays"] = conversionWindowDays

            PixelDefinition(pixelName = pixelName, params = params)
        }
    }
}

const val METRICS_PIXEL_PREFIX = "experiment_metrics"
