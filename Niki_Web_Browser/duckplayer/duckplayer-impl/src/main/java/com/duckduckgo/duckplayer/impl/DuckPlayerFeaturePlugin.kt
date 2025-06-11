

package com.duckduckgo.duckplayer.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.duckplayer.api.DuckPlayerFeatureName
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class DuckPlayerFeaturePlugin @Inject constructor(
    private val duckPlayerFeatureRepository: DuckPlayerFeatureRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        if (featureName == this.featureName) {
            duckPlayerFeatureRepository.setDuckPlayerRemoteConfigJson(jsonString)
            return true
        }
        return false
    }

    override val featureName: String = DuckPlayerFeatureName.DuckPlayer.value
}
