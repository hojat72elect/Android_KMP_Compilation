

package com.duckduckgo.app.statistics.api

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = BrowserFeatureStateReporterPlugin::class,
)
@Suppress("unused")
interface BrowserFeatureStateReporterPluginPoint
