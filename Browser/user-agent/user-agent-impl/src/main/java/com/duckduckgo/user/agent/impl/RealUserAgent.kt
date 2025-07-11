

package com.duckduckgo.user.agent.impl

import com.duckduckgo.app.browser.UriString
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.UnprotectedTemporary
import com.duckduckgo.user.agent.store.UserAgentRepository
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface UserAgent {
    /**
     * Determines if a [url] should use the "legacy" DuckDuckGo user agent.
     * The legacy user agent contains the DuckDuckGo application and Version components.
     * @return true if the [url] is in the local legacy sites list, otherwise false.
     */
    fun useLegacyUserAgent(url: String): Boolean
    fun isException(url: String): Boolean
}

@ContributesBinding(AppScope::class)
class RealUserAgent @Inject constructor(
    private val userAgentRepository: UserAgentRepository,
    private val unprotectedTemporary: UnprotectedTemporary,
) : UserAgent {

    private val legacySites = listOf("duckduckgo.com", "ddg.gg", "duck.com", "duck.it")

    override fun useLegacyUserAgent(url: String): Boolean {
        return legacySites.any { UriString.sameOrSubdomain(url, it) }
    }

    override fun isException(url: String): Boolean {
        return unprotectedTemporary.isAnException(url) || userAgentRepository.exceptions.any { UriString.sameOrSubdomain(url, it.domain) }
    }
}
