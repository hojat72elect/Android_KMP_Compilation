

package com.duckduckgo.fingerprintprotection.impl.features.fingerprintingtemporarystorage

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionFeatureName
import com.duckduckgo.fingerprintprotection.impl.fingerprintProtectionFeatureValueOf
import com.duckduckgo.fingerprintprotection.store.FingerprintingTemporaryStorageEntity
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingtemporarystorage.FingerprintingTemporaryStorageRepository
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class FingerprintingTemporaryStorageFeaturePlugin @Inject constructor(
    private val fingerprintingTemporaryStorageRepository: FingerprintingTemporaryStorageRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        val fingerprintProtectionFeatureName = fingerprintProtectionFeatureValueOf(featureName) ?: return false
        if (fingerprintProtectionFeatureName.value == this.featureName) {
            val entity = FingerprintingTemporaryStorageEntity(json = jsonString)
            fingerprintingTemporaryStorageRepository.updateAll(fingerprintingTemporaryStorageEntity = entity)
            return true
        }
        return false
    }

    override val featureName: String = FingerprintProtectionFeatureName.FingerprintingTemporaryStorage.value
}
