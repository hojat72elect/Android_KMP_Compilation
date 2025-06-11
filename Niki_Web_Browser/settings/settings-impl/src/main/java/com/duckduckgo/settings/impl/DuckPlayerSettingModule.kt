

package com.duckduckgo.settings.impl

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.settings.api.DuckPlayerSettingsPlugin

@ContributesPluginPoint(
    scope = ActivityScope::class,
    boundType = DuckPlayerSettingsPlugin::class,
)
private interface DuckPlayerSettingsPluginTrigger
