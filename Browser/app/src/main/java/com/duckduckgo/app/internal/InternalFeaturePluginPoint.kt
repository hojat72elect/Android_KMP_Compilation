

package com.duckduckgo.app.internal

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.internal.features.api.InternalFeaturePlugin

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = InternalFeaturePlugin::class,
)
@Suppress("unused")
interface InternalFeaturePluginPoint
