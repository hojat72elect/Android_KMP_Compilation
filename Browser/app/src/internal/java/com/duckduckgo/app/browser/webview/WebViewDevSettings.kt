

package com.duckduckgo.app.browser.webview



import android.content.Context
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.app.browser.R
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.internal.features.api.InternalFeaturePlugin
import com.duckduckgo.internal.features.api.InternalFeaturePlugin.Companion.WEB_VIEW_DEV_SETTINGS_PRIO_KEY
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@PriorityKey(WEB_VIEW_DEV_SETTINGS_PRIO_KEY)
class WebViewDevSettings @Inject constructor(
    private val globalActivityStarter: GlobalActivityStarter,
    private val context: Context,
) : InternalFeaturePlugin {

    override fun internalFeatureTitle(): String {
        return context.getString(R.string.webview_internal_feature_title)
    }

    override fun internalFeatureSubtitle(): String {
        return context.getString(R.string.webview_internal_feature_subtitle)
    }

    override fun onInternalFeatureClicked(activityContext: Context) {
        globalActivityStarter.start(activityContext, WebViewDevSettingsScreen)
    }
}
