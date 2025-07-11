

package com.duckduckgo.voice.api

interface VoiceSearchAvailability {
    val isVoiceSearchSupported: Boolean
    val isVoiceSearchAvailable: Boolean
    fun shouldShowVoiceSearch(
        hasFocus: Boolean = false,
        query: String = "",
        hasQueryChanged: Boolean = false,
        urlLoaded: String = "",
    ): Boolean
}
