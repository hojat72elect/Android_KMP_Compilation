

package com.duckduckgo.app.browser.cookies.db

import com.duckduckgo.app.browser.UriString
import com.duckduckgo.common.utils.DispatcherProvider
import javax.inject.Inject
import kotlinx.coroutines.withContext

class AuthCookiesAllowedDomainsRepository @Inject constructor(
    private val authCookiesAllowedDomainsDao: AuthCookiesAllowedDomainsDao,
    private val dispatcherProvider: DispatcherProvider,
) {

    suspend fun addDomain(domain: String): Long? {
        if (!UriString.isValidDomain(domain)) return null

        val authCookieAllowedDomainEntity = AuthCookieAllowedDomainEntity(domain = domain)

        val id = withContext(dispatcherProvider.io()) {
            authCookiesAllowedDomainsDao.insert(authCookieAllowedDomainEntity)
        }

        return if (id >= 0) {
            id
        } else {
            null
        }
    }

    suspend fun getDomain(domain: String): AuthCookieAllowedDomainEntity? {
        return withContext(dispatcherProvider.io()) {
            authCookiesAllowedDomainsDao.getDomain(domain)
        }
    }

    suspend fun removeDomain(authCookieAllowedDomainEntity: AuthCookieAllowedDomainEntity) {
        withContext(dispatcherProvider.io()) {
            authCookiesAllowedDomainsDao.delete(authCookieAllowedDomainEntity)
        }
    }

    suspend fun deleteAll(exceptionList: List<String> = emptyList()) {
        withContext(dispatcherProvider.io()) {
            authCookiesAllowedDomainsDao.deleteAll(exceptionList.joinToString(","))
        }
    }
}
