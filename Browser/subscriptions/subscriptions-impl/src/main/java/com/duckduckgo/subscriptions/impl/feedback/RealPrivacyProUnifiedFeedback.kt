

package com.duckduckgo.subscriptions.impl.feedback

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.subscriptions.api.PrivacyProUnifiedFeedback
import com.duckduckgo.subscriptions.api.PrivacyProUnifiedFeedback.PrivacyProFeedbackSource
import com.duckduckgo.subscriptions.api.PrivacyProUnifiedFeedback.PrivacyProFeedbackSource.DDG_SETTINGS
import com.duckduckgo.subscriptions.api.Subscriptions
import com.duckduckgo.subscriptions.impl.PrivacyProFeature
import com.duckduckgo.subscriptions.impl.repository.isActive
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealPrivacyProUnifiedFeedback @Inject constructor(
    private val subscriptions: Subscriptions,
    private val privacyProFeature: PrivacyProFeature,
) : PrivacyProUnifiedFeedback {
    override suspend fun shouldUseUnifiedFeedback(source: PrivacyProFeedbackSource): Boolean {
        return when (source) {
            DDG_SETTINGS -> privacyProFeature.useUnifiedFeedback().isEnabled() && subscriptions.getSubscriptionStatus().isActive()
            else -> privacyProFeature.useUnifiedFeedback().isEnabled()
        }
    }
}
