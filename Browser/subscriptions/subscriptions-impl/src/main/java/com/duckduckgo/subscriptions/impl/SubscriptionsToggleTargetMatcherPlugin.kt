

package com.duckduckgo.subscriptions.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.Toggle.State.Target
import com.duckduckgo.feature.toggles.api.Toggle.TargetMatcherPlugin
import com.duckduckgo.subscriptions.api.Subscriptions
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

@ContributesMultibinding(AppScope::class)
class SubscriptionsToggleTargetMatcherPlugin @Inject constructor(
    private val subscriptions: Subscriptions,
) : TargetMatcherPlugin {
    override fun matchesTargetProperty(target: Target): Boolean {
        return target.isPrivacyProEligible?.let { isPrivacyProEligible ->
            // runBlocking sucks I know :shrug:
            isPrivacyProEligible == runBlocking { subscriptions.isEligible() }
        } ?: true
    }
}
