

package com.duckduckgo.app.browser.logindetection

import android.webkit.JavascriptInterface
import timber.log.Timber

@Suppress("unused")
class LoginDetectionJavascriptInterface(private val onLoginDetected: () -> Unit) {

    @JavascriptInterface
    fun log(message: String) {
        Timber.i("LoginDetectionInterface $message")
    }

    @JavascriptInterface
    fun loginDetected() {
        onLoginDetected()
    }

    companion object {
        // Interface name used inside login_form_detection.js
        const val JAVASCRIPT_INTERFACE_NAME = "LoginDetection"
    }
}
