

package com.duckduckgo.app.pixels.campaign.params

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.AppScope

/**
 * A plugin point for additional pixel parameters that are allowed to be appended as additional parameters
 */
@ContributesPluginPoint(AppScope::class)
interface AdditionalPixelParamPlugin {
    suspend fun params(): Pair<String, String>
}
