

package com.duckduckgo.autofill.impl.systemautofill

import android.view.autofill.AutofillManager
import android.webkit.WebView
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface SystemAutofillServiceSuppressor {
    fun suppressAutofill(webView: WebView?)
}

@ContributesBinding(AppScope::class)
class RealSystemAutofillServiceSuppressor @Inject constructor() : SystemAutofillServiceSuppressor {

    override fun suppressAutofill(webView: WebView?) {
        webView?.context?.getSystemService(AutofillManager::class.java)?.cancel()
    }
}
