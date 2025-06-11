

package com.duckduckgo.newtabpage.impl

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.ContributesActivePlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.duckduckgo.newtabpage.api.NewTabPagePlugin
import com.duckduckgo.newtabpage.impl.view.NewTabPageView
import javax.inject.Inject

@ContributesActivePlugin(
    scope = AppScope::class,
    boundType = NewTabPagePlugin::class,
    priority = NewTabPagePlugin.PRIORITY_LEGACY_NTP,
    defaultActiveValue = DefaultFeatureValue.FALSE,
    supportExperiments = true,
    internalAlwaysEnabled = false,
)
class NewTabPage @Inject constructor() : NewTabPagePlugin {

    override fun getView(context: Context): View {
        return NewTabPageView(context)
    }
}
