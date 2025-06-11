

package com.duckduckgo.privacy.config.impl.features.trackerallowlist

import androidx.core.net.toUri
import com.duckduckgo.app.browser.UriString
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureToggle
import com.duckduckgo.privacy.config.api.PrivacyFeatureName
import com.duckduckgo.privacy.config.api.TrackerAllowlist
import com.duckduckgo.privacy.config.store.TrackerAllowlistEntity
import com.duckduckgo.privacy.config.store.features.trackerallowlist.TrackerAllowlistRepository
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import java.net.URI
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealTrackerAllowlist @Inject constructor(
    private val trackerAllowlistRepository: TrackerAllowlistRepository,
    private val featureToggle: FeatureToggle,
) : TrackerAllowlist {

    override fun isAnException(
        documentURL: String,
        url: String,
    ): Boolean {
        return if (featureToggle.isFeatureEnabled(PrivacyFeatureName.TrackerAllowlistFeatureName.value, true)) {
            trackerAllowlistRepository.exceptions
                .filter { UriString.sameOrSubdomain(url, it.domain) }
                .map { matches(url, documentURL, it) }
                .firstOrNull() ?: false
        } else {
            false
        }
    }

    private fun matches(
        url: String,
        documentUrl: String,
        trackerAllowlist: TrackerAllowlistEntity,
    ): Boolean {
        val cleanedUrl = removePortFromUrl(url)
        return trackerAllowlist.rules.any {
            val regex = ".*${it.rule}.*".toRegex()
            cleanedUrl.matches(regex) && (it.domains.contains("<all>") || it.domains.any { domain -> UriString.sameOrSubdomain(documentUrl, domain) })
        }
    }

    private fun removePortFromUrl(url: String): String {
        return try {
            val uri = url.toUri()
            URI(uri.scheme, uri.host, uri.path, uri.fragment).toString()
        } catch (e: Exception) {
            url
        }
    }
}
