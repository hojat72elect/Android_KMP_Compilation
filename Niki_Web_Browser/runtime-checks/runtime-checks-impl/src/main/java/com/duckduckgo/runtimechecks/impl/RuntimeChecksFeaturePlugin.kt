

package com.duckduckgo.runtimechecks.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.duckduckgo.runtimechecks.store.RuntimeChecksEntity
import com.duckduckgo.runtimechecks.store.RuntimeChecksRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class RuntimeChecksFeaturePlugin @Inject constructor(
    private val runtimeChecksRepository: RuntimeChecksRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        val runtimeChecksFeatureName = runtimeChecksFeatureValueOf(featureName) ?: return false
        if (runtimeChecksFeatureName.value == this.featureName) {
            val entity = RuntimeChecksEntity(json = jsonString)
            runtimeChecksRepository.updateAll(runtimeChecksEntity = entity)
            return true
        }
        return false
    }

    override val featureName: String = RuntimeChecksFeatureName.RuntimeChecks.value
}
