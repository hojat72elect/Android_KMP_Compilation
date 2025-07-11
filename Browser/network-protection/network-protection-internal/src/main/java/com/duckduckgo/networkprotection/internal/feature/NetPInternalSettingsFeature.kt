

package com.duckduckgo.networkprotection.internal.feature

import android.content.Context
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.internal.features.api.InternalFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@PriorityKey(InternalFeaturePlugin.VPN_SETTINGS_PRIO_KEY)
class NetPInternalSettingsFeature @Inject constructor() : InternalFeaturePlugin {
    override fun internalFeatureTitle(): String {
        return "NetP dev settings"
    }

    override fun internalFeatureSubtitle(): String {
        return "NetP dev settings for internal users"
    }

    override fun onInternalFeatureClicked(activityContext: Context) {
        activityContext.startActivity(NetPInternalSettingsActivity.intent(activityContext))
    }
}
