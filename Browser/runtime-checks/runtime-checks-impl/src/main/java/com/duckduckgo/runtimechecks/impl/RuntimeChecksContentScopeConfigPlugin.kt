

package com.duckduckgo.runtimechecks.impl

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.runtimechecks.store.RuntimeChecksRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class RuntimeChecksContentScopeConfigPlugin @Inject constructor(
    private val runtimeChecksRepository: RuntimeChecksRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = RuntimeChecksFeatureName.RuntimeChecks.value
        val config = runtimeChecksRepository.getRuntimeChecksEntity().json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
