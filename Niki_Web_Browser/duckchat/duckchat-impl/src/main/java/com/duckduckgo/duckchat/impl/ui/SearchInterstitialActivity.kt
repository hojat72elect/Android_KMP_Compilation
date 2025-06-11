

package com.duckduckgo.duckchat.impl.ui

import android.os.Bundle
import com.duckduckgo.anvil.annotations.ContributeToActivityStarter
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.app.tabs.BrowserNav
import com.duckduckgo.common.ui.DuckDuckGoActivity
import com.duckduckgo.common.ui.viewbinding.viewBinding
import com.duckduckgo.common.utils.extensions.showKeyboard
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.duckchat.api.DuckChat
import com.duckduckgo.duckchat.impl.databinding.ActivitySearchInterstitialBinding
import com.duckduckgo.navigation.api.GlobalActivityStarter
import javax.inject.Inject

object SearchInterstitialActivityParams : GlobalActivityStarter.ActivityParams

@InjectWith(ActivityScope::class)
@ContributeToActivityStarter(SearchInterstitialActivityParams::class)
class SearchInterstitialActivity : DuckDuckGoActivity() {

    @Inject
    lateinit var appBrowserNav: BrowserNav

    @Inject
    lateinit var duckChat: DuckChat

    private val binding: ActivitySearchInterstitialBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.duckChatOmnibar.apply {
            selectTab(0)
            enableFireButton = false
            enableNewChatButton = false
            onSearchSent = { query ->
                startActivity(appBrowserNav.openInCurrentTab(context, query))
                finish()
            }
            onDuckChatSent = { query ->
                duckChat.openDuckChatWithAutoPrompt(query)
                finish()
            }
            onBack = { onBackPressed() }
        }
        binding.duckChatOmnibar.duckChatInput.post {
            showKeyboard(binding.duckChatOmnibar.duckChatInput)
        }
    }
}
