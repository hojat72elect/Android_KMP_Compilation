

package com.duckduckgo.autofill.impl.deduper

import com.duckduckgo.autofill.api.domain.app.LoginCredentials

class AutofillDeduplicationLoginComparator : Comparator<LoginCredentials> {
    override fun compare(
        o1: LoginCredentials,
        o2: LoginCredentials,
    ): Int {
        val lastModifiedComparison = compareLastModified(o1.lastUpdatedMillis, o2.lastUpdatedMillis)
        if (lastModifiedComparison != 0) return lastModifiedComparison

        // last updated matches, fallback to domain
        return compareDomains(o1.domain, o2.domain)
    }

    private fun compareLastModified(
        lastModified1: Long?,
        lastModified2: Long?,
    ): Int {
        if (lastModified1 == null && lastModified2 == null) return 0

        if (lastModified1 == null) return -1
        if (lastModified2 == null) return 1
        return lastModified2.compareTo(lastModified1)
    }

    private fun compareDomains(
        domain1: String?,
        domain2: String?,
    ): Int {
        if (domain1 == null && domain2 == null) return 0
        if (domain1 == null) return -1
        if (domain2 == null) return 1
        return domain1.compareTo(domain2)
    }
}
