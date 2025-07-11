

package com.duckduckgo.fingerprintprotection.impl.features.fingerprintingcanvas

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionFeatureName
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingcanvas.FingerprintingCanvasRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class FingerprintingCanvasContentScopeConfigPlugin @Inject constructor(
    private val fingerprintingCanvasRepository: FingerprintingCanvasRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = FingerprintProtectionFeatureName.FingerprintingCanvas.value
        val config = fingerprintingCanvasRepository.fingerprintingCanvasEntity.json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
