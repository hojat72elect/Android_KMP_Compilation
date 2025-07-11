

package com.duckduckgo.app.browser.print

import android.webkit.JavascriptInterface

class PrintJavascriptInterface(private val onPrintDetected: () -> Unit) {

    @JavascriptInterface
    fun print() {
        onPrintDetected()
    }

    companion object {
        const val JAVASCRIPT_INTERFACE_NAME = "Print"
    }
}
