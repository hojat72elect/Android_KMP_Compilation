

package com.duckduckgo.fingerprintprotection.impl.features.fingerprintingcanvas

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionFeatureName
import com.duckduckgo.fingerprintprotection.impl.fingerprintProtectionFeatureValueOf
import com.duckduckgo.fingerprintprotection.store.FingerprintingCanvasEntity
import com.duckduckgo.fingerprintprotection.store.features.fingerprintingcanvas.FingerprintingCanvasRepository
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class FingerprintingCanvasFeaturePlugin @Inject constructor(
    private val fingerprintingCanvasRepository: FingerprintingCanvasRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        val fingerprintProtectionFeatureName = fingerprintProtectionFeatureValueOf(featureName) ?: return false
        if (fingerprintProtectionFeatureName.value == this.featureName) {
            val entity = FingerprintingCanvasEntity(json = jsonString)
            fingerprintingCanvasRepository.updateAll(fingerprintingCanvasEntity = entity)
            return true
        }
        return false
    }

    override val featureName: String = FingerprintProtectionFeatureName.FingerprintingCanvas.value
}
