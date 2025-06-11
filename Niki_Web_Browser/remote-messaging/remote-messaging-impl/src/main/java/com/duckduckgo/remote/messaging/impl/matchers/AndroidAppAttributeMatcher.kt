

package com.duckduckgo.remote.messaging.impl.matchers

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.browser.api.AppProperties
import com.duckduckgo.remote.messaging.api.AttributeMatcherPlugin
import com.duckduckgo.remote.messaging.api.MatchingAttribute
import com.duckduckgo.remote.messaging.impl.models.*

class AndroidAppAttributeMatcher(
    private val appProperties: AppProperties,
    private val appBuildConfig: AppBuildConfig,
) : AttributeMatcherPlugin {
    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        return when (matchingAttribute) {
            is Flavor -> {
                matchingAttribute.matches(appBuildConfig.flavor.toString())
            }
            is AppId -> {
                matchingAttribute.matches(appBuildConfig.applicationId)
            }
            is AppVersion -> {
                matchingAttribute.matches(appBuildConfig.versionName)
            }
            is Atb -> {
                matchingAttribute.matches(appProperties.atb())
            }
            is AppAtb -> {
                matchingAttribute.matches(appProperties.appAtb())
            }
            is SearchAtb -> {
                matchingAttribute.matches(appProperties.searchAtb())
            }
            is ExpVariant -> {
                matchingAttribute.matches(appProperties.expVariant())
            }
            is InstalledGPlay -> {
                matchingAttribute.matches(appProperties.installedGPlay())
            }
            else -> return null
        }
    }
}
