

package com.duckduckgo.autofill.api.emailprotection

import android.webkit.WebView

interface EmailInjector {

    fun addJsInterface(
        webView: WebView,
        onSignedInEmailProtectionPromptShown: () -> Unit,
        onInContextEmailProtectionSignupPromptShown: () -> Unit,
    )

    fun injectAddressInEmailField(
        webView: WebView,
        alias: String?,
        url: String?,
    )

    fun notifyWebAppSignEvent(
        webView: WebView,
        url: String?,
    )
}
