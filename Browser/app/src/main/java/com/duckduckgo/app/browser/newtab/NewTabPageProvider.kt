

package com.duckduckgo.app.browser.newtab

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.ContributesActivePlugin
import com.duckduckgo.anvil.annotations.ContributesActivePluginPoint
import com.duckduckgo.common.utils.plugins.ActivePluginPoint
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.newtabpage.api.NewTabPagePlugin
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface NewTabPageProvider {

    fun provideNewTabPageVersion(): Flow<NewTabPagePlugin>
}

@ContributesBinding(scope = ActivityScope::class)
class RealNewTabPageProvider @Inject constructor(
    private val newTabPageVersions: ActivePluginPoint<NewTabPagePlugin>,
) : NewTabPageProvider {
    override fun provideNewTabPageVersion(): Flow<NewTabPagePlugin> = flow {
        val newTabPage = newTabPageVersions.getPlugins().firstOrNull() ?: NewTabLegacyPage()
        emit(newTabPage)
    }
}

@ContributesActivePlugin(
    scope = AppScope::class,
    boundType = NewTabPagePlugin::class,
    priority = NewTabPagePlugin.PRIORITY_NTP,
)
class NewTabLegacyPage @Inject constructor() : NewTabPagePlugin {

    override fun getView(context: Context): View {
        return NewTabLegacyPageView(context)
    }
}

@ContributesActivePluginPoint(
    scope = AppScope::class,
    boundType = NewTabPagePlugin::class,
)
private interface NewTabPagePluginPointTrigger
