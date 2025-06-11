

package com.duckduckgo.widget

import android.content.Context
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyConfigCallbackPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = PrivacyConfigCallbackPlugin::class,
)
class WidgetPrivacyConfigUpdateListener @Inject constructor(
    private val context: Context,
    private val widgetUpdater: WidgetUpdater,
) : PrivacyConfigCallbackPlugin {

    override fun onPrivacyConfigDownloaded() {
        widgetUpdater.updateWidgets(context)
    }
}
