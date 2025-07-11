

package com.duckduckgo.fingerprintprotection.impl.features.fingerprintingscreensize

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionFeatureName
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingscreensize.FingerprintingScreenSizeRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class FingerprintingScreenSizeContentScopeConfigPlugin @Inject constructor(
    private val fingerprintingScreenSizeRepository: FingerprintingScreenSizeRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = FingerprintProtectionFeatureName.FingerprintingScreenSize.value
        val config = fingerprintingScreenSizeRepository.fingerprintingScreenSizeEntity.json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
