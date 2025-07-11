package com.duckduckgo.fingerprintprotection.impl.features.fingerprintingtemporarystorage

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionFeatureName
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingtemporarystorage.FingerprintingTemporaryStorageRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class FingerprintingTemporaryStorageContentScopeConfigPlugin @Inject constructor(
    private val fingerprintingTemporaryStorageRepository: FingerprintingTemporaryStorageRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = FingerprintProtectionFeatureName.FingerprintingTemporaryStorage.value
        val config = fingerprintingTemporaryStorageRepository.fingerprintingTemporaryStorageEntity.json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
