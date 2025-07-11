

package com.duckduckgo.fingerprintprotection.impl.features.fingerprintinghardware

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionFeatureName
import com.duckduckgo.fingerprintprotection.impl.fingerprintProtectionFeatureValueOf
import com.duckduckgo.fingerprintprotection.store.FingerprintingHardwareEntity
import com.duckduckgo.fingerprintprotection.store.features.fingerprintinghardware.FingerprintingHardwareRepository
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class FingerprintingHardwareFeaturePlugin @Inject constructor(
    private val fingerprintingHardwareRepository: FingerprintingHardwareRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        val fingerprintProtectionFeatureName = fingerprintProtectionFeatureValueOf(featureName) ?: return false
        if (fingerprintProtectionFeatureName.value == this.featureName) {
            val entity = FingerprintingHardwareEntity(json = jsonString)
            fingerprintingHardwareRepository.updateAll(fingerprintingHardwareEntity = entity)
            return true
        }
        return false
    }

    override val featureName: String = FingerprintProtectionFeatureName.FingerprintingHardware.value
}
