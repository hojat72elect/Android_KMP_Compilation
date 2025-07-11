

package com.duckduckgo.webcompat.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.duckduckgo.webcompat.store.WebCompatEntity
import com.duckduckgo.webcompat.store.WebCompatRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class WebCompatFeaturePlugin @Inject constructor(
    private val webCompatRepository: WebCompatRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        val webCompatFeatureName = webCompatFeatureValueOf(featureName) ?: return false
        if (webCompatFeatureName.value == this.featureName) {
            val entity = WebCompatEntity(json = jsonString)
            webCompatRepository.updateAll(webCompatEntity = entity)
            return true
        }
        return false
    }

    override val featureName: String = WebCompatFeatureName.WebCompat.value
}
