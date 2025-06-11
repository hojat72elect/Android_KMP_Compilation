

package com.duckduckgo.app.browser.webview

import com.duckduckgo.di.scopes.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface WebContentDebugging {
    fun isEnabled(): Boolean { return false }
}

@ContributesBinding(ActivityScope::class)
class RealWebContentDebugging @Inject constructor() : WebContentDebugging
