

package com.duckduckgo.app.browser

import androidx.webkit.WebViewFeature
import com.duckduckgo.app.browser.api.WebViewCapabilityChecker
import com.duckduckgo.app.browser.api.WebViewCapabilityChecker.WebViewCapability
import com.duckduckgo.app.browser.api.WebViewCapabilityChecker.WebViewCapability.DocumentStartJavaScript
import com.duckduckgo.app.browser.api.WebViewCapabilityChecker.WebViewCapability.WebMessageListener
import com.duckduckgo.browser.api.WebViewVersionProvider
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.extensions.compareSemanticVersion
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

@ContributesBinding(AppScope::class)
class RealWebViewCapabilityChecker @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val webViewVersionProvider: WebViewVersionProvider,
) : WebViewCapabilityChecker {

    override suspend fun isSupported(capability: WebViewCapability): Boolean {
        return when (capability) {
            DocumentStartJavaScript -> isDocumentStartJavaScriptSupported()
            WebMessageListener -> isWebMessageListenerSupported()
        }
    }

    private suspend fun isWebMessageListenerSupported(): Boolean {
        return withContext(dispatchers.io()) {
            webViewVersionProvider.getFullVersion()
                .compareSemanticVersion(WEB_MESSAGE_LISTENER_WEBVIEW_VERSION)?.let { it >= 0 } ?: false
        } && WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)
    }

    private fun isDocumentStartJavaScriptSupported(): Boolean {
        return WebViewFeature.isFeatureSupported(WebViewFeature.DOCUMENT_START_SCRIPT)
    }

    companion object {
        // critical fixes didn't exist until this WebView version
        private const val WEB_MESSAGE_LISTENER_WEBVIEW_VERSION = "126.0.6478.40"
    }
}
