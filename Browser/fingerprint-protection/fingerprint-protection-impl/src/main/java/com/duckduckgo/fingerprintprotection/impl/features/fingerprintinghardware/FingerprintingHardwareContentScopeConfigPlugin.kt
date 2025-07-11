

package com.duckduckgo.fingerprintprotection.impl.features.fingerprintinghardware

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionFeatureName
import com.duckduckgo.fingerprintprotection.store.features.fingerprintinghardware.FingerprintingHardwareRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class FingerprintingHardwareContentScopeConfigPlugin @Inject constructor(
    private val fingerprintingHardwareRepository: FingerprintingHardwareRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = FingerprintProtectionFeatureName.FingerprintingHardware.value
        val config = fingerprintingHardwareRepository.fingerprintingHardwareEntity.json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
