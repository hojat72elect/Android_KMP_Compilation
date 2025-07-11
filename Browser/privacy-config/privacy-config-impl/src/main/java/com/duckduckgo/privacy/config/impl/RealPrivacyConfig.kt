

package com.duckduckgo.privacy.config.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyConfig
import com.duckduckgo.privacy.config.api.PrivacyConfigData
import com.duckduckgo.privacy.config.store.PrivacyConfigRepository
import com.duckduckgo.privacy.config.store.toPrivacyConfigData
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealPrivacyConfig @Inject constructor(
    private val privacyConfigRepository: PrivacyConfigRepository,
) : PrivacyConfig {
    override fun privacyConfigData(): PrivacyConfigData? = privacyConfigRepository.get()?.toPrivacyConfigData()
}
