

package com.duckduckgo.app.browser.navigation

import android.webkit.WebBackForwardList
import android.webkit.WebView
import timber.log.Timber

/**
 * There is a bug in WebView whereby `webView.copyBackForwardList()` can internally throw a NPE
 *
 * This extension function can be used as a direct replacement of `copyBackForwardList()`
 * It will catch the NullPointerException and return `null` when it happens.
 *
 * https://bugs.chromium.org/p/chromium/issues/detail?id=498796
 */
fun WebView.safeCopyBackForwardList(): WebBackForwardList? {
    return try {
        copyBackForwardList()
    } catch (e: NullPointerException) {
        Timber.e(e, "Failed to extract WebView back forward list")
        null
    }
}
