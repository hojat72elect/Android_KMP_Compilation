

package com.duckduckgo.duckchat.impl.ui

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.UiThread
import com.duckduckgo.browser.api.JsInjectorPlugin
import com.duckduckgo.common.utils.plugins.PluginPoint
import javax.inject.Inject

class DuckChatWebViewClient @Inject constructor(
    private val jsPlugins: PluginPoint<JsInjectorPlugin>,
) : WebViewClient() {

    @UiThread
    override fun onPageStarted(
        webView: WebView,
        url: String?,
        favicon: Bitmap?,
    ) {
        jsPlugins.getPlugins().forEach {
            it.onPageStarted(webView, url, null)
        }
    }
}
