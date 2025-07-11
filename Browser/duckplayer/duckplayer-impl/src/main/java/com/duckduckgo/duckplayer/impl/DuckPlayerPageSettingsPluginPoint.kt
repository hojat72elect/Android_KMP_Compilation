

package com.duckduckgo.duckplayer.impl

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.duckplayer.api.DuckPlayerPageSettingsPlugin

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = DuckPlayerPageSettingsPlugin::class,
)
@Suppress("unused")
interface DuckPlayerPageSettingsPluginPoint
