

package com.duckduckgo.app.email

import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.duckduckgo.app.browser.DuckDuckGoUrlDetector
import com.duckduckgo.autofill.api.Autofill
import com.duckduckgo.autofill.api.AutofillFeature
import com.duckduckgo.autofill.api.email.EmailManager
import com.duckduckgo.common.utils.DispatcherProvider
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class EmailJavascriptInterface(
    private val emailManager: EmailManager,
    private val webView: WebView,
    private val urlDetector: DuckDuckGoUrlDetector,
    private val dispatcherProvider: DispatcherProvider,
    private val autofillFeature: AutofillFeature,
    private val autofill: Autofill,
    private val showNativeTooltip: () -> Unit,
) {

    private fun getUrl(): String? {
        return runBlocking(dispatcherProvider.main()) {
            webView.url
        }
    }

    private fun isUrlFromDuckDuckGoEmail(): Boolean {
        val url = getUrl()
        return (url != null && urlDetector.isDuckDuckGoEmailUrl(url))
    }

    private fun isAutofillEnabled() = autofillFeature.self().isEnabled()

    @JavascriptInterface
    fun isSignedIn(): String {
        return if (isUrlFromDuckDuckGoEmail()) {
            emailManager.isSignedIn().toString()
        } else {
            ""
        }
    }

    @JavascriptInterface
    fun getUserData(): String {
        return if (isUrlFromDuckDuckGoEmail()) {
            emailManager.getUserData()
        } else {
            ""
        }
    }

    @JavascriptInterface
    fun getDeviceCapabilities(): String {
        return if (isUrlFromDuckDuckGoEmail()) {
            JSONObject().apply {
                put("addUserData", true)
                put("getUserData", true)
                put("removeUserData", true)
            }.toString()
        } else {
            ""
        }
    }

    @JavascriptInterface
    fun storeCredentials(
        token: String,
        username: String,
        cohort: String,
    ) {
        if (isUrlFromDuckDuckGoEmail()) {
            emailManager.storeCredentials(token, username, cohort)
        }
    }

    @JavascriptInterface
    fun removeCredentials() {
        if (isUrlFromDuckDuckGoEmail()) {
            emailManager.signOut()
        }
    }

    @JavascriptInterface
    fun showTooltip() {
        getUrl()?.let {
            if (isAutofillEnabled() && !autofill.isAnException(it)) {
                showNativeTooltip()
            }
        }
    }

    companion object {
        const val JAVASCRIPT_INTERFACE_NAME = "EmailInterface"
    }
}
