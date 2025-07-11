

package com.duckduckgo.app.browser.print

import android.webkit.WebView
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface PrintInjector {
    fun addJsInterface(
        webView: WebView,
        onPrintDetected: () -> Unit,
    )

    fun injectPrint(
        webView: WebView,
    )
}

@ContributesBinding(AppScope::class)
class PrintInjectorJS @Inject constructor() : PrintInjector {
    override fun addJsInterface(
        webView: WebView,
        onPrintDetected: () -> Unit,
    ) {
        webView.addJavascriptInterface(PrintJavascriptInterface(onPrintDetected), PrintJavascriptInterface.JAVASCRIPT_INTERFACE_NAME)
    }

    override fun injectPrint(webView: WebView) {
        webView.loadUrl("javascript:window.print = function() { ${PrintJavascriptInterface.JAVASCRIPT_INTERFACE_NAME}.print() }")
    }
}
