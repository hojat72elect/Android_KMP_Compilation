

package com.duckduckgo.privacy.config.impl.plugins

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = PrivacyFeaturePlugin::class,
)
@Suppress("unused")
interface PrivacyFeaturePluginPoint
