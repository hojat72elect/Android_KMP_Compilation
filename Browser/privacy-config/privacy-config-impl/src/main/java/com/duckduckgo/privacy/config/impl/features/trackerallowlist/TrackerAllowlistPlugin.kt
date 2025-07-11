

package com.duckduckgo.privacy.config.impl.features.trackerallowlist

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeatureName
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.duckduckgo.privacy.config.impl.features.privacyFeatureValueOf
import com.duckduckgo.privacy.config.store.PrivacyFeatureToggles
import com.duckduckgo.privacy.config.store.PrivacyFeatureTogglesRepository
import com.duckduckgo.privacy.config.store.TrackerAllowlistEntity
import com.duckduckgo.privacy.config.store.features.trackerallowlist.TrackerAllowlistRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class TrackerAllowlistPlugin @Inject constructor(
    private val trackerAllowlistRepository: TrackerAllowlistRepository,
    private val privacyFeatureTogglesRepository: PrivacyFeatureTogglesRepository,
) : PrivacyFeaturePlugin {

    override fun store(
        featureName: String,
        jsonString: String,
    ): Boolean {
        @Suppress("NAME_SHADOWING")
        val privacyFeature = privacyFeatureValueOf(featureName) ?: return false
        if (privacyFeature.value == this.featureName) {
            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<TrackerAllowlistFeature> =
                moshi.adapter(TrackerAllowlistFeature::class.java)
            val exceptions = mutableListOf<TrackerAllowlistEntity>()

            val trackerAllowlistFeature: TrackerAllowlistFeature? = jsonAdapter.fromJson(jsonString)

            trackerAllowlistFeature?.settings?.allowlistedTrackers?.entries?.map { entry ->
                exceptions.add(TrackerAllowlistEntity(entry.key, entry.value.rules))
            }
            trackerAllowlistRepository.updateAll(exceptions)
            val isEnabled = trackerAllowlistFeature?.state == "enabled"
            privacyFeatureTogglesRepository.insert(PrivacyFeatureToggles(this.featureName, isEnabled, trackerAllowlistFeature?.minSupportedVersion))
            return true
        }
        return false
    }

    override val featureName: String = PrivacyFeatureName.TrackerAllowlistFeatureName.value
}
