

package com.duckduckgo.app.browser.serviceworker

import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import androidx.core.net.toUri
import androidx.webkit.ServiceWorkerClientCompat
import com.duckduckgo.app.browser.RequestInterceptor
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class BrowserServiceWorkerClient @Inject constructor(
    private val requestInterceptor: RequestInterceptor,
) : ServiceWorkerClientCompat() {

    override fun shouldInterceptRequest(request: WebResourceRequest): WebResourceResponse? {
        return runBlocking {
            val documentUrl: Uri? = (request.requestHeaders[HEADER_ORIGIN] ?: request.requestHeaders[HEADER_REFERER])?.toUri()
            Timber.v("Intercepting Service Worker resource ${request.url} type:${request.method} on page $documentUrl")
            requestInterceptor.shouldInterceptFromServiceWorker(request, documentUrl)
        }
    }

    companion object {
        private const val HEADER_ORIGIN = "Origin"
        private const val HEADER_REFERER = "Referer"
    }
}
