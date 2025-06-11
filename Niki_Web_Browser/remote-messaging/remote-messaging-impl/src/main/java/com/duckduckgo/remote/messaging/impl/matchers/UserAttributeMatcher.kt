

package com.duckduckgo.remote.messaging.impl.matchers

import com.duckduckgo.browser.api.UserBrowserProperties
import com.duckduckgo.remote.messaging.api.AttributeMatcherPlugin
import com.duckduckgo.remote.messaging.api.MatchingAttribute
import com.duckduckgo.remote.messaging.impl.models.*

class UserAttributeMatcher(
    private val userBrowserProperties: UserBrowserProperties,
) : AttributeMatcherPlugin {
    override suspend fun evaluate(matchingAttribute: MatchingAttribute): Boolean? {
        return when (matchingAttribute) {
            is AppTheme -> {
                matchingAttribute.matches(userBrowserProperties.appTheme().toString())
            }
            is Bookmarks -> {
                matchingAttribute.matches(userBrowserProperties.bookmarks().toInt())
            }
            is DaysSinceInstalled -> {
                matchingAttribute.matches(userBrowserProperties.daysSinceInstalled().toInt())
            }
            is DaysUsedSince -> {
                val daysUsedSince = userBrowserProperties.daysUsedSince(matchingAttribute.since)
                matchingAttribute.matches(daysUsedSince.toInt())
            }
            is DefaultBrowser -> {
                matchingAttribute.matches(userBrowserProperties.defaultBrowser())
            }
            is EmailEnabled -> {
                matchingAttribute.matches(userBrowserProperties.emailEnabled())
            }
            is Favorites -> {
                matchingAttribute.matches(userBrowserProperties.favorites().toInt())
            }
            is SearchCount -> {
                matchingAttribute.matches(userBrowserProperties.searchCount().toInt())
            }
            is WidgetAdded -> {
                matchingAttribute.matches(userBrowserProperties.widgetAdded())
            }
            else -> return null
        }
    }
}
