

package com.duckduckgo.fingerprintprotection.impl.features.fingerprintingscreensize

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionFeatureName
import com.duckduckgo.fingerprintprotection.impl.fingerprintProtectionFeatureValueOf
import com.duckduckgo.fingerprintprotection.store.FingerprintingScreenSizeEntity
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingscreensize.FingerprintingScreenSizeRepository
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class FingerprintingScreenSizeFeaturePlugin @Inject constructor(
    private val fingerprintingScreenSizeRepository: FingerprintingScreenSizeRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        val fingerprintProtectionFeatureName = fingerprintProtectionFeatureValueOf(featureName) ?: return false
        if (fingerprintProtectionFeatureName.value == this.featureName) {
            val entity = FingerprintingScreenSizeEntity(json = jsonString)
            fingerprintingScreenSizeRepository.updateAll(fingerprintingScreenSizeEntity = entity)
            return true
        }
        return false
    }

    override val featureName: String = FingerprintProtectionFeatureName.FingerprintingScreenSize.value
}
