

package com.duckduckgo.widget

import android.app.PendingIntent
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.systemsearch.SystemSearchActivity
import com.duckduckgo.voice.api.VoiceSearchAvailability
import javax.inject.Inject

class VoiceSearchWidgetConfigurator @Inject constructor(
    private val voiceSearchAvailability: VoiceSearchAvailability,
) {
    fun configureVoiceSearch(
        context: Context,
        remoteViews: RemoteViews,
        fromFavWidget: Boolean,
    ) {
        if (voiceSearchAvailability.isVoiceSearchAvailable) {
            remoteViews.setViewVisibility(R.id.voiceSearch, View.VISIBLE)
            remoteViews.setViewVisibility(R.id.search, View.GONE)
            remoteViews.setOnClickPendingIntent(R.id.voiceSearch, buildVoiceSearchPendingIntent(context, fromFavWidget))
        } else {
            remoteViews.setViewVisibility(R.id.voiceSearch, View.GONE)
            remoteViews.setViewVisibility(R.id.search, View.VISIBLE)
        }
    }

    private fun buildVoiceSearchPendingIntent(
        context: Context,
        fromFavWidget: Boolean,
    ): PendingIntent {
        val intent = if (fromFavWidget) SystemSearchActivity.fromFavWidget(context, true) else SystemSearchActivity.fromWidget(context, true)
        return PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}
