

package com.duckduckgo.subscriptions.impl.pixels

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.statistics.api.AtbLifecyclePlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.subscriptions.impl.SubscriptionsManager
import com.duckduckgo.subscriptions.impl.repository.isActive
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesMultibinding(AppScope::class)
class SubscriptionRefreshRetentionAtbPlugin @Inject constructor(
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val subscriptionsManager: SubscriptionsManager,
    private val pixelSender: SubscriptionPixelSender,
) : AtbLifecyclePlugin {

    override fun onAppRetentionAtbRefreshed(oldAtb: String, newAtb: String) {
        coroutineScope.launch {
            if (subscriptionsManager.subscriptionStatus().isActive()) {
                pixelSender.reportSubscriptionActive()
            }
        }
    }
}
