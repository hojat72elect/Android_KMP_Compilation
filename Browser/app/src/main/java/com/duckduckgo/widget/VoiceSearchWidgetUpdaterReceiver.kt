

package com.duckduckgo.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_LOCALE_CHANGED
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.di.scopes.ReceiverScope
import dagger.android.AndroidInjection
import javax.inject.Inject
import org.jetbrains.annotations.VisibleForTesting

@InjectWith(ReceiverScope::class)
class VoiceSearchWidgetUpdaterReceiver : BroadcastReceiver() {

    @Inject
    lateinit var widgetUpdater: WidgetUpdater
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        AndroidInjection.inject(this, context)
        processIntent(context, intent)
    }

    @VisibleForTesting
    fun processIntent(context: Context, intent: Intent) {
        if (intent.action == ACTION_LOCALE_CHANGED) {
            widgetUpdater.updateWidgets(context)
        }
    }
}
