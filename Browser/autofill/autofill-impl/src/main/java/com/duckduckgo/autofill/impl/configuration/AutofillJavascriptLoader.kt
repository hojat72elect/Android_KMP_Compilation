

package com.duckduckgo.autofill.impl.configuration

import com.duckduckgo.autofill.impl.configuration.AutofillJavascriptEnvironmentConfiguration.AutofillJsConfigType
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import java.io.BufferedReader
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface AutofillJavascriptLoader {
    suspend fun getAutofillJavascript(): String
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DefaultAutofillJavascriptLoader @Inject constructor(
    private val filenameProvider: AutofillJavascriptEnvironmentConfiguration,
    private val dispatchers: DispatcherProvider,
) : AutofillJavascriptLoader {

    private val productionJavascript: String by lazy { loadJs(AUTOFILL_JS_FILENAME) }
    private val debugJavascript: String by lazy { loadJs(AUTOFILL_JS_DEBUG_FILENAME) }

    override suspend fun getAutofillJavascript(): String {
        return withContext(dispatchers.io()) {
            when (filenameProvider.getConfigType()) {
                AutofillJsConfigType.Production -> productionJavascript
                AutofillJsConfigType.Debug -> debugJavascript
            }
        }
    }

    private fun loadJs(resourceName: String): String = readResource(resourceName).use { it?.readText() }.orEmpty()

    private fun readResource(resourceName: String): BufferedReader? {
        return javaClass.classLoader?.getResource(resourceName)?.openStream()?.bufferedReader()
    }

    companion object {
        const val AUTOFILL_JS_FILENAME = "autofill.js"
        const val AUTOFILL_JS_DEBUG_FILENAME = "autofill-debug.js"
    }
}
