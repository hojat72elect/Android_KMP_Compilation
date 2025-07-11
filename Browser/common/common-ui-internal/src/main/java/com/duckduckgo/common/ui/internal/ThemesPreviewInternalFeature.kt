

package com.duckduckgo.common.ui.internal

import android.content.Context
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.common.ui.themepreview.ui.AppComponentsActivity
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.internal.features.api.InternalFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@PriorityKey(InternalFeaturePlugin.ADS_SETTINGS_PRIO_KEY)
class ThemesPreviewInternalFeature @Inject constructor() : InternalFeaturePlugin {
    override fun internalFeatureTitle(): String {
        return "Android Design System Preview"
    }

    override fun internalFeatureSubtitle(): String {
        return "Set of components designed following our Design System"
    }

    override fun onInternalFeatureClicked(activityContext: Context) {
        activityContext.startActivity(AppComponentsActivity.intent(activityContext))
    }
}
