
package com.duckduckgo.autofill.impl.configuration

import android.webkit.WebView
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.autofill.api.AutofillCapabilityChecker
import com.duckduckgo.autofill.api.BrowserAutofill.Configurator
import com.duckduckgo.common.utils.DefaultDispatcherProvider
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@ContributesBinding(AppScope::class)
class InlineBrowserAutofillConfigurator @Inject constructor(
    private val autofillRuntimeConfigProvider: AutofillRuntimeConfigProvider,
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider(),
    private val autofillCapabilityChecker: AutofillCapabilityChecker,
    private val autofillJavascriptLoader: AutofillJavascriptLoader,
) : Configurator {
    override fun configureAutofillForCurrentPage(
        webView: WebView,
        url: String?,
    ) {
        coroutineScope.launch(dispatchers.io()) {
            if (canJsBeInjected(url)) {
                Timber.v("Injecting autofill JS into WebView for %s", url)

                val rawJs = autofillJavascriptLoader.getAutofillJavascript()
                val formatted = autofillRuntimeConfigProvider.getRuntimeConfiguration(rawJs, url)

                withContext(dispatchers.main()) {
                    webView.evaluateJavascript("javascript:$formatted", null)
                }
            } else {
                Timber.v("Won't inject autofill JS into WebView for: %s", url)
            }
        }
    }

    private suspend fun canJsBeInjected(url: String?): Boolean {
        url?.let {
            // note, we don't check for autofillEnabledByUser here, as the user-facing preference doesn't cover email
            return autofillCapabilityChecker.isAutofillEnabledByConfiguration(it)
        }
        return false
    }
}
