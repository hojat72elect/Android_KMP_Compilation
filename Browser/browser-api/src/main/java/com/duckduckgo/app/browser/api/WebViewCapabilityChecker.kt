

package com.duckduckgo.app.browser.api

/**
 * Allows WebView capabilities to be queried.
 * WebView capabilities depend on various conditions, such as WebView version, feature flags etc...
 * Capabilities can change over time, so it's recommended to always check immediately before trying to use that capability.
 */
interface WebViewCapabilityChecker {

    /**
     * Check if a particular capability is currently supported by the WebView
     */
    suspend fun isSupported(capability: WebViewCapability): Boolean

    /**
     * WebView capabilities, which can be provided to [isSupported]
     */
    sealed interface WebViewCapability {
        /**
         * WebMessageListener
         * The ability to post web messages to JS, and receive web messages from JS
         */
        data object WebMessageListener : WebViewCapability

        /**
         * DocumentStartJavaScript
         * The ability to inject Javascript which is guaranteed to be executed first on the page, and available in all iframes
         */
        data object DocumentStartJavaScript : WebViewCapability
    }
}
