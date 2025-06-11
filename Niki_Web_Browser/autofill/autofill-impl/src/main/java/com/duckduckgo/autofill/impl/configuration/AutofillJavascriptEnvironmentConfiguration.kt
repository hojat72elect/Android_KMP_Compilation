

package com.duckduckgo.autofill.impl.configuration

import com.duckduckgo.autofill.impl.configuration.AutofillJavascriptEnvironmentConfiguration.AutofillJsConfigType
import com.duckduckgo.autofill.impl.configuration.AutofillJavascriptEnvironmentConfiguration.AutofillJsConfigType.Debug
import com.duckduckgo.autofill.impl.configuration.AutofillJavascriptEnvironmentConfiguration.AutofillJsConfigType.Production
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

interface AutofillJavascriptEnvironmentConfiguration {

    fun useProductionConfig()
    fun useDebugConfig()
    fun getConfigType(): AutofillJsConfigType

    sealed interface AutofillJsConfigType {
        data object Production : AutofillJsConfigType
        data object Debug : AutofillJsConfigType
    }
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DefaultAutofillJavascriptEnvironmentConfiguration @Inject constructor() : AutofillJavascriptEnvironmentConfiguration {

    private var configType: AutofillJsConfigType = Production

    override fun useProductionConfig() {
        configType = Production
    }

    override fun useDebugConfig() {
        configType = Debug
    }

    override fun getConfigType(): AutofillJsConfigType = configType
}
