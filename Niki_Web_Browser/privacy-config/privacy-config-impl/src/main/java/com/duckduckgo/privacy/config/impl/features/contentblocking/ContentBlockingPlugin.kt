

package com.duckduckgo.privacy.config.impl.features.contentblocking

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeatureName
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.duckduckgo.privacy.config.impl.features.privacyFeatureValueOf
import com.duckduckgo.privacy.config.store.ContentBlockingExceptionEntity
import com.duckduckgo.privacy.config.store.PrivacyFeatureToggles
import com.duckduckgo.privacy.config.store.PrivacyFeatureTogglesRepository
import com.duckduckgo.privacy.config.store.features.contentblocking.ContentBlockingRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class ContentBlockingPlugin @Inject constructor(
    private val contentBlockingRepository: ContentBlockingRepository,
    private val privacyFeatureTogglesRepository: PrivacyFeatureTogglesRepository,
) : PrivacyFeaturePlugin {

    override fun store(
        featureName: String,
        jsonString: String,
    ): Boolean {
        @Suppress("NAME_SHADOWING")
        val privacyFeature = privacyFeatureValueOf(featureName) ?: return false
        if (privacyFeature.value == this.featureName) {
            val contentBlockingExceptions = mutableListOf<ContentBlockingExceptionEntity>()
            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<ContentBlockingFeature> =
                moshi.adapter(ContentBlockingFeature::class.java)

            val contentBlockingFeature: ContentBlockingFeature? = jsonAdapter.fromJson(jsonString)
            contentBlockingFeature?.exceptions?.map {
                contentBlockingExceptions.add(ContentBlockingExceptionEntity(it.domain, it.reason.orEmpty()))
            }
            contentBlockingRepository.updateAll(contentBlockingExceptions)
            val isEnabled = contentBlockingFeature?.state == "enabled"
            privacyFeatureTogglesRepository.insert(PrivacyFeatureToggles(this.featureName, isEnabled, contentBlockingFeature?.minSupportedVersion))
            return true
        }
        return false
    }

    override val featureName: String = PrivacyFeatureName.ContentBlockingFeatureName.value
}
