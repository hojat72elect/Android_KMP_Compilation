

package com.duckduckgo.settings.impl

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.settings.api.ProSettingsPlugin

@ContributesPluginPoint(
    scope = ActivityScope::class,
    boundType = ProSettingsPlugin::class,
)
private interface ProSettingsPluginTrigger
