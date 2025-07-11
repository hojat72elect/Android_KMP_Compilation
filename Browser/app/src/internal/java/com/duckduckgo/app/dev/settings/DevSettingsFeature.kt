

package com.duckduckgo.app.dev.settings

import android.content.Context
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.app.browser.R
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.internal.features.api.InternalFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@PriorityKey(InternalFeaturePlugin.DEVELOPER_SETTINGS_PRIO_KEY)
class DevSettingsFeature @Inject constructor(private val context: Context) : InternalFeaturePlugin {
    override fun internalFeatureTitle(): String {
        return context.getString(R.string.devSettingsTitle)
    }

    override fun internalFeatureSubtitle(): String {
        return context.getString(R.string.devSettingsSubtitle)
    }

    override fun onInternalFeatureClicked(activityContext: Context) {
        activityContext.startActivity(DevSettingsActivity.intent(activityContext))
    }
}
