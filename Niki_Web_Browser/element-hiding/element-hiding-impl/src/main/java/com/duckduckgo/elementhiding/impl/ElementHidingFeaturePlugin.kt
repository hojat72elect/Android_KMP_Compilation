

package com.duckduckgo.elementhiding.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.elementhiding.store.ElementHidingEntity
import com.duckduckgo.elementhiding.store.ElementHidingRepository
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class ElementHidingFeaturePlugin @Inject constructor(
    private val elementHidingRepository: ElementHidingRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        val elementHidingFeatureName = elementHidingFeatureValueOf(featureName) ?: return false
        if (elementHidingFeatureName.value == this.featureName) {
            val entity = ElementHidingEntity(json = jsonString)
            elementHidingRepository.updateAll(elementHidingEntity = entity)
            return true
        }
        return false
    }

    override val featureName: String = ElementHidingFeatureName.ElementHiding.value
}
