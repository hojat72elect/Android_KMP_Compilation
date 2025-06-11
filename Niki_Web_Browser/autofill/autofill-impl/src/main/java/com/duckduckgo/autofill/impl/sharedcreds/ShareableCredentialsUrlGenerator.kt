

package com.duckduckgo.autofill.impl.sharedcreds

import com.duckduckgo.autofill.impl.sharedcreds.SharedCredentialsParser.SharedCredentialConfig
import com.duckduckgo.autofill.impl.urlmatcher.AutofillUrlMatcher
import com.duckduckgo.autofill.impl.urlmatcher.AutofillUrlMatcher.ExtractedUrlParts
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

interface ShareableCredentialsUrlGenerator {
    fun generateShareableUrls(
        sourceUrl: String,
        config: SharedCredentialConfig,
    ): List<ExtractedUrlParts>
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class RealShareableCredentialsUrlGenerator @Inject constructor(
    private val autofillUrlMatcher: AutofillUrlMatcher,
) : ShareableCredentialsUrlGenerator {

    override fun generateShareableUrls(
        sourceUrl: String,
        config: SharedCredentialConfig,
    ): List<ExtractedUrlParts> {
        val visitedSiteUrlParts = autofillUrlMatcher.extractUrlPartsForAutofill(sourceUrl)

        val omnidirectionalMatches = omnidirectionalShareableUrls(config, visitedSiteUrlParts)
        val unidirectionalMatches = unidirectionalShareableUrls(config, visitedSiteUrlParts)

        return (omnidirectionalMatches + unidirectionalMatches).distinct()
    }

    private fun unidirectionalShareableUrls(
        config: SharedCredentialConfig,
        visitedSiteUrlParts: ExtractedUrlParts,
    ): MutableList<ExtractedUrlParts> {
        val unidirectionalMatches = mutableListOf<ExtractedUrlParts>()
        config.unidirectionalRules.forEach { rule ->

            val matches = rule.to.filter { autofillUrlMatcher.matchingForAutofill(visitedSiteUrlParts, it) }
            matches.forEach { _ ->
                unidirectionalMatches.addAll(rule.from.removeExactMatch(visitedSiteUrlParts))
            }
        }
        return unidirectionalMatches
    }

    private fun omnidirectionalShareableUrls(
        config: SharedCredentialConfig,
        visitedSiteUrlParts: ExtractedUrlParts,
    ): MutableList<ExtractedUrlParts> {
        val omnidirectionalMatches = mutableListOf<ExtractedUrlParts>()
        config.omnidirectionalRules.forEach { rule ->

            val matches = rule.shared.filter { autofillUrlMatcher.matchingForAutofill(visitedSiteUrlParts, it) }
            matches.forEach { _ ->
                omnidirectionalMatches.addAll(rule.shared.removeExactMatch(visitedSiteUrlParts))
            }
        }
        return omnidirectionalMatches
    }

    private fun List<ExtractedUrlParts>.removeExactMatch(visitedSiteUrlParts: ExtractedUrlParts): List<ExtractedUrlParts> {
        return filterNot { it.eTldPlus1 == visitedSiteUrlParts.eTldPlus1 }
    }
}
