

package com.duckduckgo.app.downloads

import android.content.Context
import com.duckduckgo.anvil.annotations.ContributesActivePlugin
import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.downloads.DownloadsScreens.DownloadsScreenNoParams
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.newtabpage.api.NewTabPageShortcutPlugin
import com.duckduckgo.newtabpage.api.NewTabShortcut
import javax.inject.Inject

@ContributesActivePlugin(
    AppScope::class,
    boundType = NewTabPageShortcutPlugin::class,
    priority = NewTabPageShortcutPlugin.PRIORITY_DOWNLOADS,
)
class DownloadsNewTabShortcutPlugin @Inject constructor(
    private val globalActivityStarter: GlobalActivityStarter,
    private val setting: DownloadsNewTabShortcutSetting,
) : NewTabPageShortcutPlugin {

    inner class DownloadsShortcut() : NewTabShortcut {
        override fun name(): String = "downloads"
        override fun titleResource(): Int = R.string.newTabPageShortcutDownloads
        override fun iconResource(): Int = R.drawable.ic_shortcut_downloads
    }

    override fun getShortcut(): NewTabShortcut {
        return DownloadsShortcut()
    }

    override fun onClick(context: Context) {
        globalActivityStarter.start(context, DownloadsScreenNoParams)
    }

    override suspend fun isUserEnabled(): Boolean {
        return setting.self().isEnabled()
    }

    override suspend fun setUserEnabled(enabled: Boolean) {
        if (enabled) {
            setting.self().setRawStoredState(Toggle.State(true))
        } else {
            setting.self().setRawStoredState(Toggle.State(false))
        }
    }
}

/**
 * Local feature/settings - they will never be in remote config
 */
@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "downloadsNewTabShortcutSetting",
)
interface DownloadsNewTabShortcutSetting {
    @Toggle.DefaultValue(DefaultFeatureValue.TRUE)
    fun self(): Toggle
}
