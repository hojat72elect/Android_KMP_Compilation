

package com.duckduckgo.browser.api

interface WebViewVersionProvider {
    companion object {
        const val WEBVIEW_UNKNOWN_VERSION = "unknown"
    }

    /**
     * Returns the full version of WebView or unknown if unable to get it
     * @returns a string [String] with the full version of WebView
     */
    fun getFullVersion(): String

    /**
     * Returns the major version of WebView or unknown if unable to get it
     * @returns a string [String] with the major version of WebView
     */
    fun getMajorVersion(): String
}
