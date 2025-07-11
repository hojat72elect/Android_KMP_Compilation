

package com.duckduckgo.app.browser

import com.duckduckgo.browser.api.WebViewVersionProvider
import com.duckduckgo.browser.api.WebViewVersionProvider.Companion.WEBVIEW_UNKNOWN_VERSION
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DefaultWebViewVersionProvider @Inject constructor(
    private val webViewVersionSource: WebViewVersionSource,
) : WebViewVersionProvider {
    companion object {
        const val WEBVIEW_VERSION_DELIMITER = "."
    }

    override fun getFullVersion(): String = webViewVersionSource.get().mapEmptyToUnknown()

    override fun getMajorVersion(): String =
        webViewVersionSource.get().captureMajorVersion().mapNonIntegerToUnknown()

    private fun String.captureMajorVersion() = this.split(WEBVIEW_VERSION_DELIMITER)[0]

    private fun String.mapNonIntegerToUnknown() =
        if (isNotBlank() && all(Char::isDigit)) this else WEBVIEW_UNKNOWN_VERSION

    private fun String.mapEmptyToUnknown() = ifBlank { WEBVIEW_UNKNOWN_VERSION }
}
