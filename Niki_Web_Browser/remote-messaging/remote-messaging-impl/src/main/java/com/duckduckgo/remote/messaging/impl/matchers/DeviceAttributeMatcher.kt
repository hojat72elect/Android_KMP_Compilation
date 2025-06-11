

package com.duckduckgo.remote.messaging.impl.matchers

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.browser.api.AppProperties
import com.duckduckgo.remote.messaging.api.AttributeMatcherPlugin
import com.duckduckgo.remote.messaging.api.MatchingAttribute
import com.duckduckgo.remote.messaging.impl.models.*

class DeviceAttributeMatcher(
    private val appBuildConfig: AppBuildConfig,
    private val appProperties: AppProperties,
) : AttributeMatcherPlugin {
    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        return when (matchingAttribute) {
            is Api -> {
                matchingAttribute.matches(appBuildConfig.sdkInt)
            }
            is Locale -> {
                matchingAttribute.matches(appBuildConfig.deviceLocale.asJsonFormat())
            }
            is WebView -> {
                matchingAttribute.matches(appProperties.webView())
            }
            else -> return null
        }
    }
}
