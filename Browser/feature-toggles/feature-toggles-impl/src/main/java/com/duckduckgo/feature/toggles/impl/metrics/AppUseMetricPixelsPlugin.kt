

package com.duckduckgo.feature.toggles.impl.metrics

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.ConversionWindow
import com.duckduckgo.feature.toggles.api.FeatureTogglesInventory
import com.duckduckgo.feature.toggles.api.MetricsPixel
import com.duckduckgo.feature.toggles.api.MetricsPixelPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class AppUseMetricPixelsPlugin @Inject constructor(private val inventory: FeatureTogglesInventory) : MetricsPixelPlugin {

    override suspend fun getMetrics(): List<MetricsPixel> {
        return inventory.getAllActiveExperimentToggles().flatMap { toggle ->
            listOf(
                MetricsPixel(
                    metric = "app_use",
                    value = "1",
                    toggle = toggle,
                    conversionWindow = (1..7).map { ConversionWindow(lowerWindow = it, upperWindow = it) } +
                        ConversionWindow(lowerWindow = 5, upperWindow = 7),
                ),
                MetricsPixel(
                    metric = "app_use",
                    value = "4",
                    toggle = toggle,
                    conversionWindow = listOf(
                        ConversionWindow(lowerWindow = 5, upperWindow = 7),
                        ConversionWindow(lowerWindow = 8, upperWindow = 15),
                    ),
                ),
                MetricsPixel(
                    metric = "app_use",
                    value = "6",
                    toggle = toggle,
                    conversionWindow = listOf(
                        ConversionWindow(lowerWindow = 5, upperWindow = 7),
                        ConversionWindow(lowerWindow = 8, upperWindow = 15),
                    ),
                ),
                MetricsPixel(
                    metric = "app_use",
                    value = "11",
                    toggle = toggle,
                    conversionWindow = listOf(
                        ConversionWindow(lowerWindow = 5, upperWindow = 7),
                        ConversionWindow(lowerWindow = 8, upperWindow = 15),
                    ),
                ),
                MetricsPixel(
                    metric = "app_use",
                    value = "21",
                    toggle = toggle,
                    conversionWindow = listOf(
                        ConversionWindow(lowerWindow = 5, upperWindow = 7),
                        ConversionWindow(lowerWindow = 8, upperWindow = 15),
                    ),
                ),
                MetricsPixel(
                    metric = "app_use",
                    value = "30",
                    toggle = toggle,
                    conversionWindow = listOf(
                        ConversionWindow(lowerWindow = 5, upperWindow = 7),
                        ConversionWindow(lowerWindow = 8, upperWindow = 15),
                    ),
                ),
            )
        }
    }
}
