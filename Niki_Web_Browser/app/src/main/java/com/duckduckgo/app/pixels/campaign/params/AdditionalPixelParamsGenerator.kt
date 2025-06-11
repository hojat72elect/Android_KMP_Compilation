

package com.duckduckgo.app.pixels.campaign.params

import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface AdditionalPixelParamsGenerator {
    /**
     * @return a subset of additional parameters that could be appended to eligible pixels
     */
    suspend fun generateAdditionalParams(): Map<String, String>
}

@ContributesBinding(AppScope::class)
class RealAdditionalPixelParamsGenerator @Inject constructor(
    private val pluginPoint: PluginPoint<AdditionalPixelParamPlugin>,
) : AdditionalPixelParamsGenerator {
    override suspend fun generateAdditionalParams(): Map<String, String> {
        val size: Int = (pluginPoint.getPlugins().size * 2) / 3
        return pluginPoint.getPlugins().shuffled().subList(0, size).associate {
            it.params()
        }
    }
}
