

package com.duckduckgo.subscriptions.impl.feedback

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.subscriptions.api.PrivacyProUnifiedFeedback
import com.duckduckgo.subscriptions.api.PrivacyProUnifiedFeedback.PrivacyProFeedbackSource
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class DummyPrivacyProUnifiedFeedback @Inject constructor() : PrivacyProUnifiedFeedback {
    override suspend fun shouldUseUnifiedFeedback(source: PrivacyProFeedbackSource): Boolean {
        return false
    }
}
