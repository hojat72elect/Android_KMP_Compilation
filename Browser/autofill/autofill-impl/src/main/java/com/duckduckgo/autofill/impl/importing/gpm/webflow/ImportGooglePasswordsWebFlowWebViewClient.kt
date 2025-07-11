

package com.duckduckgo.autofill.impl.importing.gpm.webflow

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import javax.inject.Inject

class ImportGooglePasswordsWebFlowWebViewClient @Inject constructor(
    private val callback: NewPageCallback,
) : WebViewClient() {

    interface NewPageCallback {
        fun onPageStarted(url: String?) {}
        fun onPageFinished(url: String?) {}
    }

    override fun onPageStarted(
        view: WebView?,
        url: String?,
        favicon: Bitmap?,
    ) {
        callback.onPageStarted(url)
    }

    override fun onPageFinished(
        view: WebView?,
        url: String?,
    ) {
        callback.onPageFinished(url)
    }
}
