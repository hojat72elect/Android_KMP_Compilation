

package com.duckduckgo.privacy.config.impl.features.gpc

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeatureName
import com.duckduckgo.privacy.config.store.features.gpc.GpcRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class GpcContentScopeConfigPlugin @Inject constructor(
    private val gpcRepository: GpcRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = PrivacyFeatureName.GpcFeatureName.value
        val config = gpcRepository.gpcContentScopeConfig
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return "\"globalPrivacyControlValue\":${gpcRepository.isGpcEnabled()}"
    }
}
