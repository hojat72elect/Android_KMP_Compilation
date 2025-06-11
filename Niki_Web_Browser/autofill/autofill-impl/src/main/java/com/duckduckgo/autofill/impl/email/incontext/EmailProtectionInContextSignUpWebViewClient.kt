

package com.duckduckgo.autofill.impl.email.incontext

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import javax.inject.Inject

class EmailProtectionInContextSignUpWebViewClient @Inject constructor(
    private val callback: NewPageCallback,
) : WebViewClient() {

    interface NewPageCallback {
        fun onPageStarted(url: String)
    }

    override fun onPageStarted(
        view: WebView?,
        url: String?,
        favicon: Bitmap?,
    ) {
        url?.let { callback.onPageStarted(it) }
    }
}
