

package com.duckduckgo.privacy.config.impl.features.amplinks

import com.duckduckgo.app.browser.UriString
import com.duckduckgo.app.privacy.db.UserAllowListRepository
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureToggle
import com.duckduckgo.privacy.config.api.AmpLinkInfo
import com.duckduckgo.privacy.config.api.AmpLinkType
import com.duckduckgo.privacy.config.api.AmpLinks
import com.duckduckgo.privacy.config.api.PrivacyFeatureName
import com.duckduckgo.privacy.config.api.UnprotectedTemporary
import com.duckduckgo.privacy.config.store.features.amplinks.AmpLinksRepository
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import timber.log.Timber

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealAmpLinks @Inject constructor(
    private val ampLinksRepository: AmpLinksRepository,
    private val featureToggle: FeatureToggle,
    private val unprotectedTemporary: UnprotectedTemporary,
    private val userAllowListRepository: UserAllowListRepository,
) : AmpLinks {

    private var lastExtractedUrl: String? = null

    override var lastAmpLinkInfo: AmpLinkInfo? = null

    override fun isAnException(url: String): Boolean {
        return matches(url) || unprotectedTemporary.isAnException(url) || userAllowListRepository.isUrlInUserAllowList(url)
    }

    private fun matches(url: String): Boolean {
        return ampLinksRepository.exceptions.any { UriString.sameOrSubdomain(url, it.domain) }
    }

    override fun extractCanonicalFromAmpLink(url: String): AmpLinkType? {
        if (!featureToggle.isFeatureEnabled(PrivacyFeatureName.AmpLinksFeatureName.value)) return null
        if (url == lastExtractedUrl) return null

        val extractedUrl = extractCanonical(url)

        lastExtractedUrl = extractedUrl

        extractedUrl?.let {
            return if (isAnException(extractedUrl)) {
                null
            } else {
                lastAmpLinkInfo = AmpLinkInfo(ampLink = url)
                AmpLinkType.ExtractedAmpLink(extractedUrl = extractedUrl)
            }
        }

        if (urlContainsAmpKeyword(url)) {
            return if (isAnException(url)) {
                null
            } else {
                AmpLinkType.CloakedAmpLink(ampUrl = url)
            }
        }
        return null
    }

    private fun urlContainsAmpKeyword(url: String): Boolean {
        val ampKeywords = ampLinksRepository.ampKeywords

        ampKeywords.forEach { keyword ->
            if (url.contains(keyword)) {
                return true
            }
        }
        return false
    }

    private fun extractCanonical(url: String): String? {
        val ampFormat = urlIsExtractableAmpLink(url) ?: return null
        val matchResult = ampFormat.find(url) ?: return null

        val groups = matchResult.groups
        if (groups.size < 2) return null

        var destinationUrl = groups[1]?.value ?: return null

        if (!destinationUrl.startsWith("http")) {
            destinationUrl = "https://$destinationUrl"
        }
        return destinationUrl
    }

    override fun processDestinationUrl(initialUrl: String, extractedUrl: String?): String {
        return if (extractedUrl != null && isValidDestinationUrl(extractedUrl)) {
            lastAmpLinkInfo = AmpLinkInfo(ampLink = initialUrl)
            Timber.d("AMP link detection: Success! Loading extracted URL: $extractedUrl")
            extractedUrl
        } else {
            Timber.d("AMP link detection: Failed! Loading initial URL: $initialUrl")
            initialUrl
        }
    }

    private fun isValidDestinationUrl(extractedUrl: String): Boolean {
        return !isAnException(extractedUrl) && (extractedUrl.startsWith("http") || extractedUrl.startsWith("https"))
    }

    private fun urlIsExtractableAmpLink(url: String): Regex? {
        val ampLinkFormats = ampLinksRepository.ampLinkFormats

        ampLinkFormats.forEach { format ->
            if (url.matches(format)) {
                return format
            }
        }
        return null
    }
}
