

package com.duckduckgo.autofill.impl.email.incontext

import android.webkit.WebChromeClient
import android.webkit.WebView

class EmailProtectionInContextSignUpWebChromeClient(
    private val callback: ProgressListener,
) : WebChromeClient() {

    interface ProgressListener {
        fun onPageFinished(url: String)
    }

    override fun onProgressChanged(
        webView: WebView?,
        newProgress: Int,
    ) {
        val url = webView?.url ?: return

        when (newProgress) {
            PROGRESS_PAGE_FINISH -> callback.onPageFinished(url)
        }
    }

    companion object {
        private const val PROGRESS_PAGE_FINISH = 100
    }
}
