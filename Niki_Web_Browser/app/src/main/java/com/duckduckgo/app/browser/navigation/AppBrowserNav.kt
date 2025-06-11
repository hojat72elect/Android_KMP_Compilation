

package com.duckduckgo.app.browser.navigation

import android.content.Context
import android.content.Intent
import com.duckduckgo.app.browser.BrowserActivity
import com.duckduckgo.app.tabs.BrowserNav
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class AppBrowserNav @Inject constructor() : BrowserNav {
    override fun openInNewTab(
        context: Context,
        url: String,
    ): Intent {
        return BrowserActivity.intent(context = context, queryExtra = url, interstitialScreen = true)
    }

    override fun openInCurrentTab(
        context: Context,
        url: String,
    ): Intent {
        return BrowserActivity.intent(context = context, queryExtra = url, openInCurrentTab = true)
    }
}
