

package com.duckduckgo.common.ui.internal.experiments

import android.content.Context
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.internal.features.api.InternalFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@PriorityKey(InternalFeaturePlugin.EXPERIMENTAL_UI_SETTINGS)
class ExperimentalUIInternalFeature @Inject constructor() : InternalFeaturePlugin {
    override fun internalFeatureTitle(): String {
        return "Experimental UI Settings"
    }

    override fun internalFeatureSubtitle(): String {
        return "Test the latest UI developments"
    }

    override fun onInternalFeatureClicked(activityContext: Context) {
        activityContext.startActivity(UIExperimentsActivity.intent(activityContext))
    }
}
