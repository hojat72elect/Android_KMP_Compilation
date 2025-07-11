

package com.duckduckgo.subscriptions.internal

import android.content.Context
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.internal.features.api.InternalFeaturePlugin
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.subscriptions.internal.SubsInternalScreens.InternalSettings
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@PriorityKey(InternalFeaturePlugin.SUBS_SETTINGS_PRIO_KEY)
class InternalSettingsActivity @Inject constructor(
    private val globalActivityStarter: GlobalActivityStarter,
) : InternalFeaturePlugin {
    override fun internalFeatureTitle(): String {
        return "Subscription Settings"
    }

    override fun internalFeatureSubtitle(): String {
        return "Subscription dev settings for internal users"
    }

    override fun onInternalFeatureClicked(activityContext: Context) {
        globalActivityStarter.start(activityContext, InternalSettings)
    }
}
