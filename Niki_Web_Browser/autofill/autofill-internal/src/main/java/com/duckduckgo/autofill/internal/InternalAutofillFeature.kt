

package com.duckduckgo.autofill.internal

import android.content.Context
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.internal.features.api.InternalFeaturePlugin
import com.duckduckgo.internal.features.api.InternalFeaturePlugin.Companion.AUTOFILL_SETTINGS_PRIO_KEY
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@PriorityKey(AUTOFILL_SETTINGS_PRIO_KEY)
class InternalAutofillFeature @Inject constructor(private val context: Context) : InternalFeaturePlugin {
    override fun internalFeatureTitle(): String {
        return context.getString(R.string.autofillDevSettingsTitle)
    }

    override fun internalFeatureSubtitle(): String {
        return context.getString(R.string.autofillDevSettingsSubtitle)
    }

    override fun onInternalFeatureClicked(activityContext: Context) {
        activityContext.startActivity(AutofillInternalSettingsActivity.intent(activityContext))
    }
}
