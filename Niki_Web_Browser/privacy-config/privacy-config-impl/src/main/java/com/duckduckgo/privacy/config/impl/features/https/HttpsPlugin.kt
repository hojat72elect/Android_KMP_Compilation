

package com.duckduckgo.privacy.config.impl.features.https

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeatureName
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.duckduckgo.privacy.config.impl.features.privacyFeatureValueOf
import com.duckduckgo.privacy.config.store.HttpsExceptionEntity
import com.duckduckgo.privacy.config.store.PrivacyFeatureToggles
import com.duckduckgo.privacy.config.store.PrivacyFeatureTogglesRepository
import com.duckduckgo.privacy.config.store.features.https.HttpsRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class HttpsPlugin @Inject constructor(
    private val httpsRepository: HttpsRepository,
    private val privacyFeatureTogglesRepository: PrivacyFeatureTogglesRepository,
) : PrivacyFeaturePlugin {

    override fun store(
        featureName: String,
        jsonString: String,
    ): Boolean {
        @Suppress("NAME_SHADOWING")
        val privacyFeature = privacyFeatureValueOf(featureName) ?: return false
        if (privacyFeature.value == this.featureName) {
            val httpsExceptions = mutableListOf<HttpsExceptionEntity>()
            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<HttpsFeature> =
                moshi.adapter(HttpsFeature::class.java)

            val httpsFeature: HttpsFeature? = jsonAdapter.fromJson(jsonString)
            httpsFeature?.exceptions?.map {
                httpsExceptions.add(HttpsExceptionEntity(it.domain, it.reason.orEmpty()))
            }
            httpsRepository.updateAll(httpsExceptions)
            val isEnabled = httpsFeature?.state == "enabled"
            privacyFeatureTogglesRepository.insert(PrivacyFeatureToggles(this.featureName, isEnabled, httpsFeature?.minSupportedVersion))
            return true
        }
        return false
    }

    override val featureName: String = PrivacyFeatureName.HttpsFeatureName.value
}
