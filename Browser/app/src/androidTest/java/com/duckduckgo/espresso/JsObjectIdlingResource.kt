

package com.duckduckgo.espresso

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.webkit.WebView
import androidx.test.espresso.IdlingResource

class JsObjectIdlingResource(
    private val webView: WebView,
    private val objectName: String,
) : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    private var isIdle = false

    private val handler = Handler(Looper.getMainLooper())
    private val checkInterval = 100L // milliseconds
    private val timeoutMillis = 20_000L // 20 seconds
    private val startTime = SystemClock.elapsedRealtime()

    override fun getName(): String = "JsObjectIdlingResource for $objectName"

    override fun isIdleNow(): Boolean = isIdle

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
        pollForJsObject()
    }

    private fun pollForJsObject() {
        if (SystemClock.elapsedRealtime() - startTime > timeoutMillis) {
            isIdle = true
            callback?.onTransitionToIdle()
            // fail test if the object is not found within the timeout
            throw AssertionError("JS object '$objectName' did not appear within timeout.")
        }

        webView.evaluateJavascript(
            "(typeof $objectName !== 'undefined')",
        ) { result ->
            if (result == "true") {
                isIdle = true
                callback?.onTransitionToIdle()
            } else {
                handler.postDelayed({ pollForJsObject() }, checkInterval)
            }
        }
    }
}
