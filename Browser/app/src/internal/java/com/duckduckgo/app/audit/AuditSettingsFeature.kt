

package com.duckduckgo.app.audit

import android.content.Context
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.app.browser.R
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.internal.features.api.InternalFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@PriorityKey(InternalFeaturePlugin.AUDIT_SETTINGS_PRIO_KEY)
class AuditSettingsFeature @Inject constructor(private val context: Context) : InternalFeaturePlugin {
    override fun internalFeatureTitle(): String {
        return context.getString(R.string.auditSettingsTitle)
    }

    override fun internalFeatureSubtitle(): String {
        return context.getString(R.string.auditSettingsSubtitle)
    }

    override fun onInternalFeatureClicked(activityContext: Context) {
        activityContext.startActivity(AuditSettingsActivity.intent(activityContext))
    }
}
