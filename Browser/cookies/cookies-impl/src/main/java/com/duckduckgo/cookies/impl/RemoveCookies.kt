

package com.duckduckgo.cookies.impl

import com.duckduckgo.cookies.api.CookieRemover
import com.duckduckgo.cookies.api.RemoveCookiesStrategy
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import javax.inject.Named

@ContributesBinding(AppScope::class)
class RemoveCookies @Inject constructor(
    @Named("cookieManagerRemover") private val cookieManagerRemover: CookieRemover,
    @Named("sqlCookieRemover") private val selectiveCookieRemover: CookieRemover,
) : RemoveCookiesStrategy {
    override suspend fun removeCookies() {
        val removeSuccess = selectiveCookieRemover.removeCookies()
        if (!removeSuccess) {
            cookieManagerRemover.removeCookies()
        }
    }
}
