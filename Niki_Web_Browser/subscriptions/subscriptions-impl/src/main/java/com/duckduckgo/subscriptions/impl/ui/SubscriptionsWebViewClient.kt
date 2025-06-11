

package com.duckduckgo.subscriptions.impl.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.duckduckgo.app.browser.SpecialUrlDetector

class SubscriptionsWebViewClient(
    private val specialUrlDetector: SpecialUrlDetector,
    private val context: Context,
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(
        view: WebView,
        request: WebResourceRequest,
    ): Boolean {
        val url = request.url
        return shouldOverride(view, url)
    }

    private fun shouldOverride(
        webView: WebView,
        url: Uri,
    ): Boolean {
        return try {
            when (val urlType = specialUrlDetector.determineType(initiatingUrl = webView.originalUrl, uri = url)) {
                is SpecialUrlDetector.UrlType.Telephone -> {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${urlType.telephoneNumber}")
                    context.startActivity(intent)
                    true
                }
                else -> false
            }
        } catch (e: Throwable) {
            false
        }
    }
}
