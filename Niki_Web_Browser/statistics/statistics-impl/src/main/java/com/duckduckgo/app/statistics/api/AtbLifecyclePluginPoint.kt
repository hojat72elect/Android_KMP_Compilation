

package com.duckduckgo.app.statistics.api

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = AtbLifecyclePlugin::class,
)
@Suppress("unused")
interface AtbLifecyclePluginPoint
