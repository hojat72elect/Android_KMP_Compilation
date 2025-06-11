

package com.duckduckgo.voice.impl

import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.voice.api.VoiceSearchAvailabilityPixelLogger
import com.duckduckgo.voice.store.VoiceSearchRepository
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(ActivityScope::class)
class RealVoiceSearchAvailabilityPixelLogger @Inject constructor(
    private val pixel: Pixel,
    private val voiceSearchRepository: VoiceSearchRepository,
) : VoiceSearchAvailabilityPixelLogger {

    override fun log() {
        if (!voiceSearchRepository.getHasLoggedAvailability()) {
            pixel.fire(VoiceSearchPixelNames.VOICE_SEARCH_AVAILABLE)
            voiceSearchRepository.saveLoggedAvailability()
        }
    }
}
