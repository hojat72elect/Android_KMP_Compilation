

package com.duckduckgo.voice.impl

import com.duckduckgo.app.statistics.api.BrowserFeatureStateReporterPlugin
import com.duckduckgo.app.statistics.pixels.Pixel.PixelParameter
import com.duckduckgo.common.utils.extensions.toBinaryString
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.voice.api.VoiceSearchAvailability
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

interface VoiceStateReporterPlugin

@ContributesMultibinding(scope = AppScope::class, boundType = BrowserFeatureStateReporterPlugin::class)
@ContributesBinding(scope = AppScope::class, boundType = VoiceStateReporterPlugin::class)
class RealVoiceStateReporterPlugin @Inject constructor(
    private val voiceSearchAvailability: VoiceSearchAvailability,
) : VoiceStateReporterPlugin, BrowserFeatureStateReporterPlugin {
    override fun featureStateParams(): Map<String, String> {
        return mapOf(PixelParameter.VOICE_SEARCH to voiceSearchAvailability.isVoiceSearchAvailable.toBinaryString())
    }
}
