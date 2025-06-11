

package com.duckduckgo.privacy.config.impl.features.amplinks

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeatureName
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.duckduckgo.privacy.config.impl.features.privacyFeatureValueOf
import com.duckduckgo.privacy.config.store.*
import com.duckduckgo.privacy.config.store.features.amplinks.AmpLinksRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class AmpLinksPlugin @Inject constructor(
    private val ampLinksRepository: AmpLinksRepository,
    private val privacyFeatureTogglesRepository: PrivacyFeatureTogglesRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        @Suppress("NAME_SHADOWING")
        val privacyFeature = privacyFeatureValueOf(featureName) ?: return false
        if (privacyFeature.value == this.featureName) {
            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<AmpLinksFeature> =
                moshi.adapter(AmpLinksFeature::class.java)

            val exceptions = mutableListOf<AmpLinkExceptionEntity>()
            val ampLinkFormats = mutableListOf<AmpLinkFormatEntity>()
            val ampKeywords = mutableListOf<AmpKeywordEntity>()

            val ampLinksFeature: AmpLinksFeature? = jsonAdapter.fromJson(jsonString)

            ampLinksFeature?.exceptions?.map {
                exceptions.add(AmpLinkExceptionEntity(it.domain, it.reason.orEmpty()))
            }

            ampLinksFeature?.settings?.linkFormats?.map {
                ampLinkFormats.add(AmpLinkFormatEntity(it))
            }

            ampLinksFeature?.settings?.keywords?.map {
                ampKeywords.add(AmpKeywordEntity(it))
            }

            ampLinksRepository.updateAll(exceptions, ampLinkFormats, ampKeywords)
            val isEnabled = ampLinksFeature?.state == "enabled"
            privacyFeatureTogglesRepository.insert(PrivacyFeatureToggles(this.featureName, isEnabled, ampLinksFeature?.minSupportedVersion))
            return true
        }
        return false
    }

    override val featureName: String = PrivacyFeatureName.AmpLinksFeatureName.value
}
