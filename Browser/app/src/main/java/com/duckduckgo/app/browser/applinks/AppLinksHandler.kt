

package com.duckduckgo.app.browser.applinks

import com.duckduckgo.app.browser.UriString
import com.duckduckgo.common.utils.extractDomain
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface AppLinksHandler {
    fun handleAppLink(
        isForMainFrame: Boolean,
        urlString: String,
        appLinksEnabled: Boolean,
        shouldHaltWebNavigation: Boolean,
        launchAppLink: () -> Unit,
    ): Boolean

    fun updatePreviousUrl(urlString: String?)
    fun setUserQueryState(state: Boolean)
    fun isUserQuery(): Boolean
}

@ContributesBinding(AppScope::class)
class DuckDuckGoAppLinksHandler @Inject constructor() : AppLinksHandler {

    var previousUrl: String? = null
    var isAUserQuery = false
    var hasTriggeredForDomain = false
    private val alwaysTriggerList = listOf("app.digid.nl")

    override fun handleAppLink(
        isForMainFrame: Boolean,
        urlString: String,
        appLinksEnabled: Boolean,
        shouldHaltWebNavigation: Boolean,
        launchAppLink: () -> Unit,
    ): Boolean {
        if (!appLinksEnabled || !isForMainFrame) {
            return false
        }

        previousUrl?.let {
            if (isSameOrSubdomain(it, urlString)) {
                val shouldTrigger = alwaysTriggerList.contains(urlString.extractDomain())
                if (isAUserQuery || !hasTriggeredForDomain || shouldTrigger) {
                    previousUrl = urlString
                    launchAppLink()
                    hasTriggeredForDomain = true
                    if (shouldTrigger) return true
                }
                return false
            }
        }

        previousUrl = urlString
        launchAppLink()
        hasTriggeredForDomain = true
        return shouldHaltWebNavigation
    }

    private fun isSameOrSubdomain(
        previousUrlString: String,
        currentUrlString: String,
    ) = UriString.sameOrSubdomain(previousUrlString, currentUrlString) || UriString.sameOrSubdomain(currentUrlString, previousUrlString)

    override fun updatePreviousUrl(urlString: String?) {
        if (urlString == null || previousUrl?.let { isSameOrSubdomain(it, urlString) } == false) {
            hasTriggeredForDomain = false
        }
        previousUrl = urlString
    }

    override fun setUserQueryState(state: Boolean) {
        isAUserQuery = state
    }

    override fun isUserQuery(): Boolean {
        return isAUserQuery
    }
}
