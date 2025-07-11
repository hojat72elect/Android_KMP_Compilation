

package com.duckduckgo.cookies.impl.thirdpartycookienames

import com.duckduckgo.cookies.api.ThirdPartyCookieNames
import com.duckduckgo.cookies.store.CookiesRepository
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealThirdPartyCookieNames @Inject constructor(
    private val cookiesRepository: CookiesRepository,
) : ThirdPartyCookieNames {

    override fun hasExcludedCookieName(cookieString: String): Boolean {
        return cookiesRepository.cookieNames.any { cookieString.contains(it) }
    }
}
