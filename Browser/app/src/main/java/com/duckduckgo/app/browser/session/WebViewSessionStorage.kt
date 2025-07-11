

package com.duckduckgo.app.browser.session

import android.os.Bundle
import android.os.Parcel
import android.util.LruCache
import android.webkit.WebView
import timber.log.Timber

interface WebViewSessionStorage {
    fun saveSession(
        webView: WebView?,
        tabId: String,
    )

    fun restoreSession(
        webView: WebView?,
        tabId: String,
    ): Boolean

    fun deleteSession(tabId: String)
    fun deleteAllSessions()
}

class WebViewSessionInMemoryStorage : WebViewSessionStorage {

    private val cache = object : LruCache<String, Bundle>(CACHE_SIZE_BYTES) {

        /**
         * Size (in bytes) of a single entry in the cache for the given key.
         * We specify the max cache size in bytes, so we need to calculate an approximate size of the cache entry in bytes.
         */
        override fun sizeOf(
            key: String,
            bundle: Bundle,
        ) = bundle.sizeInBytes()

        override fun entryRemoved(
            evicted: Boolean,
            key: String?,
            oldValue: Bundle?,
            newValue: Bundle?,
        ) {
            if (evicted) {
                Timber.v("Evicted $key from WebView session storage")
            }
        }
    }

    override fun saveSession(
        webView: WebView?,
        tabId: String,
    ) {
        if (webView == null) {
            Timber.w("WebView is null; cannot save session")
            return
        }

        Timber.i("Saving WebView session for $tabId")

        val webViewBundle = createWebViewBundle(webView)

        val bundle = Bundle()
        bundle.putBundle(CACHE_KEY_WEBVIEW, webViewBundle)
        bundle.putInt(CACHE_KEY_SCROLL_POSITION, webView.scrollY)
        cache.put(tabId, bundle)

        Timber.d("Stored ${bundle.sizeInBytes()} bytes for WebView $webView")
        logCacheSize()
    }

    private fun createWebViewBundle(webView: WebView): Bundle {
        return Bundle().also {
            webView.saveState(it)
        }
    }

    override fun restoreSession(
        webView: WebView?,
        tabId: String,
    ): Boolean {
        if (webView == null) {
            Timber.w("WebView is null; cannot restore session")
            return false
        }

        Timber.i("Restoring WebView session for $tabId")

        val bundle = cache[tabId]
        if (bundle == null) {
            Timber.v("No saved bundle for tab $tabId")
            return false
        }

        val webViewBundle = bundle.getBundle(CACHE_KEY_WEBVIEW)
        webViewBundle?.let {
            webView.restoreState(it)
        }
        webView.scrollY = bundle.getInt(CACHE_KEY_SCROLL_POSITION)
        cache.remove(tabId)

        logCacheSize()

        return true
    }

    override fun deleteSession(tabId: String) {
        cache.remove(tabId)
        Timber.i("Deleted web session for $tabId")
        logCacheSize()
    }

    override fun deleteAllSessions() {
        cache.evictAll()
        logCacheSize()
    }

    private fun logCacheSize() {
        Timber.v("Cache size is now ~${cache.size()} bytes out of a max size of ${cache.maxSize()} bytes")
    }

    private fun Bundle.sizeInBytes(): Int {
        val parcel = Parcel.obtain()
        parcel.writeValue(this)

        val bytes = parcel.marshall()
        parcel.recycle()

        return bytes.size
    }

    companion object {
        private const val CACHE_SIZE_BYTES = 10 * 1024 * 1024 // 10 MiB

        private const val CACHE_KEY_WEBVIEW = "webview"
        private const val CACHE_KEY_SCROLL_POSITION = "scroll-position"
    }
}
