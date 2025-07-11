package com.duckduckgo.contentscopescripts.impl

import android.webkit.WebView
import com.duckduckgo.app.global.model.Site
import com.duckduckgo.browser.api.JsInjectorPlugin
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class ContentScopeScriptsJsInjectorPlugin @Inject constructor(
    private val coreContentScopeScripts: CoreContentScopeScripts,
) : JsInjectorPlugin {
    override fun onPageStarted(webView: WebView, url: String?, site: Site?) {
        if (coreContentScopeScripts.isEnabled()) {
            webView.evaluateJavascript("javascript:${coreContentScopeScripts.getScript(site)}", null)
        }
    }

    override fun onPageFinished(webView: WebView, url: String?, site: Site?) {
        // NOOP
    }
}
