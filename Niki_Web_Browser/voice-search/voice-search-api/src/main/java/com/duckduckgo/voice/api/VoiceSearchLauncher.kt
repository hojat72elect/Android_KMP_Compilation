

package com.duckduckgo.voice.api

import android.app.Activity
import androidx.activity.result.ActivityResultCaller

interface VoiceSearchLauncher {
    fun registerResultsCallback(
        caller: ActivityResultCaller,
        activity: Activity,
        source: Source,
        onEvent: (Event) -> Unit,
    )

    fun launch(activity: Activity)

    enum class Source(val paramValueName: String) {
        BROWSER("browser"),
        WIDGET("widget"),
    }

    sealed class Event {
        data class VoiceRecognitionSuccess(val result: String) : Event()
        object SearchCancelled : Event()
        object VoiceSearchDisabled : Event()
    }
}
