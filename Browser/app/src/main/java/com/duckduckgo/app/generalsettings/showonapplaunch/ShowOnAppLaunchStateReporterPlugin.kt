

package com.duckduckgo.app.generalsettings.showonapplaunch

import com.duckduckgo.app.generalsettings.showonapplaunch.model.ShowOnAppLaunchOption
import com.duckduckgo.app.generalsettings.showonapplaunch.store.ShowOnAppLaunchOptionDataStore
import com.duckduckgo.app.statistics.api.BrowserFeatureStateReporterPlugin
import com.duckduckgo.app.statistics.pixels.Pixel.PixelParameter
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

interface ShowOnAppLaunchReporterPlugin

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = BrowserFeatureStateReporterPlugin::class,
)
@ContributesBinding(scope = AppScope::class, boundType = ShowOnAppLaunchReporterPlugin::class)
class ShowOnAppLaunchStateReporterPlugin
@Inject
constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val showOnAppLaunchOptionDataStore: ShowOnAppLaunchOptionDataStore,
) : ShowOnAppLaunchReporterPlugin, BrowserFeatureStateReporterPlugin {

    override fun featureStateParams(): Map<String, String> {
        val option =
            runBlocking(dispatcherProvider.io()) {
                showOnAppLaunchOptionDataStore.optionFlow.first()
            }
        val dailyPixelValue = ShowOnAppLaunchOption.getDailyPixelValue(option)
        return mapOf(PixelParameter.LAUNCH_SCREEN to dailyPixelValue)
    }
}
