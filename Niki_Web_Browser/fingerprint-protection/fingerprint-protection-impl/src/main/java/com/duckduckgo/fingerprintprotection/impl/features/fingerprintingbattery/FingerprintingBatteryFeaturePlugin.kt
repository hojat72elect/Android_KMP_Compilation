

package com.duckduckgo.fingerprintprotection.impl.features.fingerprintingbattery

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionFeatureName
import com.duckduckgo.fingerprintprotection.impl.fingerprintProtectionFeatureValueOf
import com.duckduckgo.fingerprintprotection.store.FingerprintingBatteryEntity
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingbattery.FingerprintingBatteryRepository
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class FingerprintingBatteryFeaturePlugin @Inject constructor(
    private val fingerprintingBatteryRepository: FingerprintingBatteryRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        val fingerprintProtectionFeatureName = fingerprintProtectionFeatureValueOf(featureName) ?: return false
        if (fingerprintProtectionFeatureName.value == this.featureName) {
            val entity = FingerprintingBatteryEntity(json = jsonString)
            fingerprintingBatteryRepository.updateAll(fingerprintingBatteryEntity = entity)
            return true
        }
        return false
    }

    override val featureName: String = FingerprintProtectionFeatureName.FingerprintingBattery.value
}
