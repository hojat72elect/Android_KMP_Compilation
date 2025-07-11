

package com.duckduckgo.sync.internal

import android.content.Context
import android.content.Intent
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.internal.features.api.InternalFeaturePlugin
import com.duckduckgo.sync.impl.ui.SyncInternalSettingsActivity
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@PriorityKey(InternalFeaturePlugin.SYNC_SETTINGS_PRIO_KEY)
class InternalSyncFeature @Inject constructor(private val context: Context) : InternalFeaturePlugin {
    override fun internalFeatureTitle(): String {
        return context.getString(R.string.syncSettingsTitle)
    }

    override fun internalFeatureSubtitle(): String {
        return context.getString(R.string.syncSettingsSubtitle)
    }

    override fun onInternalFeatureClicked(activityContext: Context) {
        activityContext.startActivity(Intent(activityContext, SyncInternalSettingsActivity::class.java))
    }
}
