

package com.duckduckgo.newtabpage.impl.shortcuts

import android.content.Context
import com.duckduckgo.anvil.annotations.ContributesActivePlugin
import com.duckduckgo.anvil.annotations.ContributesRemoteFeature
import com.duckduckgo.app.tabs.BrowserNav
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle
import com.duckduckgo.feature.toggles.api.Toggle.DefaultFeatureValue
import com.duckduckgo.newtabpage.api.NewTabPageShortcutPlugin
import com.duckduckgo.newtabpage.api.NewTabShortcut
import com.duckduckgo.newtabpage.impl.R
import javax.inject.Inject

@ContributesActivePlugin(
    AppScope::class,
    boundType = NewTabPageShortcutPlugin::class,
    priority = NewTabPageShortcutPlugin.PRIORITY_AI_CHAT,
)
class AIChatNewTabShortcutPlugin @Inject constructor(
    private val browserNav: BrowserNav,
    private val setting: AIChatNewTabShortcutSetting,
) : NewTabPageShortcutPlugin {

    inner class AIChatShortcut() : NewTabShortcut {
        override fun name(): String = "chat"
        override fun titleResource(): Int = R.string.newTabPageShortcutChat
        override fun iconResource(): Int = R.drawable.ic_shortcuts_ai_chat
    }

    override fun getShortcut(): NewTabShortcut {
        return AIChatShortcut()
    }

    override fun onClick(context: Context) {
        context.startActivity(browserNav.openInCurrentTab(context, AI_CHAT_URL))
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

    companion object {
        private const val AI_CHAT_URL = "https://duckduckgo.com/chat"
    }
}

/**
 * Local feature/settings - they will never be in remote config
 */
@ContributesRemoteFeature(
    scope = AppScope::class,
    featureName = "aIChatNewTabShortcutSetting",
)
interface AIChatNewTabShortcutSetting {
    @Toggle.DefaultValue(DefaultFeatureValue.FALSE)
    fun self(): Toggle
}
