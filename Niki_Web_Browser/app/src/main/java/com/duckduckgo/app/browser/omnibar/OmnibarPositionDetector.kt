

package com.duckduckgo.app.browser.omnibar

import com.duckduckgo.app.settings.db.SettingsDataStore
import com.duckduckgo.app.statistics.api.BrowserFeatureStateReporterPlugin
import com.duckduckgo.app.statistics.pixels.Pixel.PixelParameter
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import javax.inject.Inject

interface OmnibarPositionReporterPlugin

@ContributesMultibinding(scope = AppScope::class, boundType = BrowserFeatureStateReporterPlugin::class)
@ContributesBinding(scope = AppScope::class, boundType = OmnibarPositionReporterPlugin::class)
@SingleInstanceIn(AppScope::class)
class OmnibarPositionDetector @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
) : OmnibarPositionReporterPlugin, BrowserFeatureStateReporterPlugin {
    override fun featureStateParams(): Map<String, String> {
        return mapOf(PixelParameter.ADDRESS_BAR to settingsDataStore.omnibarPosition.name)
    }
}
