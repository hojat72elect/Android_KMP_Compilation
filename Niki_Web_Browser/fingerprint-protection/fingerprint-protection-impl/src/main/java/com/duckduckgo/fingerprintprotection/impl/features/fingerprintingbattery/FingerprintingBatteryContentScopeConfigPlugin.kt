

package com.duckduckgo.fingerprintprotection.impl.features.fingerprintingbattery

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionFeatureName
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingbattery.FingerprintingBatteryRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class FingerprintingBatteryContentScopeConfigPlugin @Inject constructor(
    private val fingerprintingBatteryRepository: FingerprintingBatteryRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = FingerprintProtectionFeatureName.FingerprintingBattery.value
        val config = fingerprintingBatteryRepository.fingerprintingBatteryEntity.json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
