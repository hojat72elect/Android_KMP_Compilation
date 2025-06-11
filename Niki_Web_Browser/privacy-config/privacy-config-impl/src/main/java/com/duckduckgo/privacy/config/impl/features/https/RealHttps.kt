

package com.duckduckgo.privacy.config.impl.features.https

import com.duckduckgo.app.browser.UriString.Companion.sameOrSubdomain
import com.duckduckgo.app.privacy.db.UserAllowListRepository
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.Https
import com.duckduckgo.privacy.config.api.UnprotectedTemporary
import com.duckduckgo.privacy.config.store.features.https.HttpsRepository
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealHttps @Inject constructor(
    private val httpsRepository: HttpsRepository,
    private val unprotectedTemporary: UnprotectedTemporary,
    private val userAllowListRepository: UserAllowListRepository,
) : Https {

    override fun isAnException(url: String): Boolean {
        return unprotectedTemporary.isAnException(url) || matches(url) || userAllowListRepository.isUrlInUserAllowList(url)
    }

    private fun matches(url: String): Boolean {
        return httpsRepository.exceptions.any { sameOrSubdomain(url, it.domain) }
    }
}
