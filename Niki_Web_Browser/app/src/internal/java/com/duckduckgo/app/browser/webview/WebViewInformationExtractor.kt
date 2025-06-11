

package com.duckduckgo.app.browser.webview

import android.content.Context
import androidx.webkit.WebViewCompat
import com.duckduckgo.app.browser.webview.WebViewInformationExtractor.WebViewData
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface WebViewInformationExtractor {

    data class WebViewData(
        val webViewVersion: String,
        val webViewPackageName: String,
    )

    fun extract(): WebViewData
}

@ContributesBinding(AppScope::class)
class InternalWebViewInformationExtractor @Inject constructor(
    private val context: Context,
) : WebViewInformationExtractor {

    override fun extract(): WebViewData {
        return WebViewCompat.getCurrentWebViewPackage(context)?.let {
            WebViewData(
                webViewVersion = it.versionName ?: UNKNOWN,
                webViewPackageName = it.packageName ?: UNKNOWN,
            )
        } ?: unknownWebView
    }

    companion object {
        private const val UNKNOWN = "unknown"

        private val unknownWebView = WebViewData(
            webViewVersion = UNKNOWN,
            webViewPackageName = UNKNOWN,
        )
    }
}
