

package com.duckduckgo.app.browser.certificates

import com.duckduckgo.di.scopes.AppScope
import dagger.SingleInstanceIn
import javax.inject.Inject

interface BypassedSSLCertificatesRepository {

    fun add(domain: String)

    fun contains(domain: String): Boolean
}

@SingleInstanceIn(AppScope::class)
class RealBypassedSSLCertificatesRepository @Inject constructor() : BypassedSSLCertificatesRepository {

    private val trustedSites: MutableList<String> = mutableListOf()
    override fun add(domain: String) {
        trustedSites.add(domain)
    }

    override fun contains(domain: String): Boolean {
        return trustedSites.contains(domain)
    }
}
