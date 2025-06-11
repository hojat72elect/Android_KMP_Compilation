

package com.duckduckgo.privacy.config.impl.plugins

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyConfigCallbackPlugin

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = PrivacyConfigCallbackPlugin::class,
)
interface PrivacyConfigCallbackPluginPoint
