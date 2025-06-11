

package com.duckduckgo.privacy.dashboard.impl.di

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.dashboard.api.PrivacyProtectionTogglePlugin

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = PrivacyProtectionTogglePlugin::class,
)
@Suppress("unused")
internal interface PrivacyProtectionTogglePluginPoint
