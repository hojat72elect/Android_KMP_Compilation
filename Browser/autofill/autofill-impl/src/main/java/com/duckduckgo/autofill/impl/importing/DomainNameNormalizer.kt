

package com.duckduckgo.autofill.impl.importing

import com.duckduckgo.autofill.impl.urlmatcher.AutofillUrlMatcher
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface DomainNameNormalizer {
    suspend fun normalize(unnormalizedUrl: String?): String?
}

@ContributesBinding(AppScope::class)
class DefaultDomainNameNormalizer @Inject constructor(
    private val urlMatcher: AutofillUrlMatcher,
) : DomainNameNormalizer {
    override suspend fun normalize(unnormalizedUrl: String?): String? {
        return if (unnormalizedUrl == null) {
            null
        } else {
            urlMatcher.cleanRawUrl(unnormalizedUrl)
        }
    }
}
