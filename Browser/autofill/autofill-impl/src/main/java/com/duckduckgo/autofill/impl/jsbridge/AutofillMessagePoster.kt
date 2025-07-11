

package com.duckduckgo.autofill.impl.jsbridge

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.core.net.toUri
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext
import timber.log.Timber

interface AutofillMessagePoster {
    suspend fun postMessage(
        webView: WebView?,
        message: String,
    )
}

@ContributesBinding(AppScope::class)
class AutofillWebViewMessagePoster @Inject constructor(
    private val dispatchers: DispatcherProvider,
) : AutofillMessagePoster {

    @SuppressLint("RequiresFeature")
    override suspend fun postMessage(
        webView: WebView?,
        message: String,
    ) {
        webView?.let { wv ->
            withContext(dispatchers.main()) {
                if (!WebViewFeature.isFeatureSupported(WebViewFeature.POST_WEB_MESSAGE)) {
                    Timber.e("Unable to post web message")
                    return@withContext
                }

                WebViewCompat.postWebMessage(wv, WebMessageCompat(message), WILDCARD_ORIGIN_URL)
            }
        }
    }

    companion object {
        private val WILDCARD_ORIGIN_URL = "*".toUri()
    }
}
