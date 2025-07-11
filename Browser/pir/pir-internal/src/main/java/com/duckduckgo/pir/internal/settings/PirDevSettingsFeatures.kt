

package com.duckduckgo.pir.internal.settings

import android.content.Context
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.internal.features.api.InternalFeaturePlugin
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@PriorityKey(InternalFeaturePlugin.PIR_SETTINGS_PRIO_KEY)
class PirDevSettingsFeatures @Inject constructor(
    private val globalActivityStarter: GlobalActivityStarter,
) : InternalFeaturePlugin {
    override fun internalFeatureTitle(): String {
        return "PIR dev settings"
    }

    override fun internalFeatureSubtitle(): String {
        return "PIR dev settings for internal users"
    }

    override fun onInternalFeatureClicked(activityContext: Context) {
        globalActivityStarter.start(activityContext, PirSettingsScreenNoParams)
    }
}
