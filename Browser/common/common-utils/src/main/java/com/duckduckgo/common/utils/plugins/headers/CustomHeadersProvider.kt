

package com.duckduckgo.common.utils.plugins.headers

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface CustomHeadersProvider {

    /**
     * Returns a [Map] of custom headers that should be added to the request.
     * @param url The url of the request.
     * @return A [Map] of headers.
     */
    fun getCustomHeaders(url: String): Map<String, String>

    /**
     * A plugin point for custom headers that should be added to all requests.
     */
    @ContributesPluginPoint(AppScope::class)
    interface CustomHeadersPlugin {

        /**
         * Returns a [Map] of headers that should be added to the request IF the url passed allows for them to be
         * added.
         * @param url The url of the request.
         * @return A [Map] of headers.
         */
        fun getHeaders(url: String): Map<String, String>
    }
}

@ContributesBinding(AppScope::class)
class RealCustomHeadersProvider @Inject constructor(
    private val customHeadersPluginPoint: PluginPoint<CustomHeadersProvider.CustomHeadersPlugin>,
) : CustomHeadersProvider {

    override fun getCustomHeaders(url: String): Map<String, String> {
        val customHeaders = mutableMapOf<String, String>()
        customHeadersPluginPoint.getPlugins().forEach {
            customHeaders.putAll(it.getHeaders(url))
        }
        return customHeaders.toMap()
    }
}
