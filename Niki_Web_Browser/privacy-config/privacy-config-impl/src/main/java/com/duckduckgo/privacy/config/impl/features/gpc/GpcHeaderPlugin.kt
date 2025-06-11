

package com.duckduckgo.privacy.config.impl.features.gpc

import com.duckduckgo.common.utils.plugins.headers.CustomHeadersProvider.CustomHeadersPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.Gpc
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(scope = AppScope::class)
class GpcHeaderPlugin @Inject constructor(
    private val gpc: Gpc,
) : CustomHeadersPlugin {

    override fun getHeaders(url: String): Map<String, String> {
        return gpc.getHeaders(url)
    }
}
