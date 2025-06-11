

package com.duckduckgo.espresso

import android.webkit.WebView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback

class WebViewIdlingResource(private val webView: WebView) : IdlingResource {

    private lateinit var resourceCallback: ResourceCallback

    override fun getName(): String = javaClass.name

    override fun isIdleNow(): Boolean {
        if (webView.progress == 100) {
            resourceCallback.onTransitionToIdle()
            return true
        }
        return false
    }

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        this.resourceCallback = callback
    }
}
