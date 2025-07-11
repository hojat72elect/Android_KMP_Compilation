

package com.duckduckgo.widget

import android.content.Context
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.voice.api.VoiceSearchStatusListener
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class WidgetVoiceSearchStatusListener @Inject constructor(
    private val context: Context,
    private val widgetUpdater: WidgetUpdater,
) : VoiceSearchStatusListener {

    override fun voiceSearchStatusChanged() {
        widgetUpdater.updateWidgets(context)
    }
}
