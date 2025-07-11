

package com.duckduckgo.subscriptions.impl

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.subscriptions.impl.SubscriptionsConstants.BASIC_SUBSCRIPTION
import com.duckduckgo.subscriptions.impl.billing.PlayBillingManager
import com.duckduckgo.subscriptions.impl.repository.AuthRepository
import com.duckduckgo.subscriptions.impl.services.SubscriptionsService
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import logcat.logcat

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class SubscriptionFeaturesFetcher @Inject constructor(
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val playBillingManager: PlayBillingManager,
    private val subscriptionsService: SubscriptionsService,
    private val authRepository: AuthRepository,
    private val privacyProFeature: PrivacyProFeature,
    private val dispatcherProvider: DispatcherProvider,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        appCoroutineScope.launch {
            try {
                if (isFeaturesApiEnabled()) {
                    fetchSubscriptionFeatures()
                }
            } catch (e: Exception) {
                logcat { "Failed to fetch subscription features" }
            }
        }
    }

    private suspend fun isFeaturesApiEnabled(): Boolean = withContext(dispatcherProvider.io()) {
        privacyProFeature.featuresApi().isEnabled()
    }

    private suspend fun fetchSubscriptionFeatures() {
        playBillingManager.productsFlow
            .firstOrNull() { it.isNotEmpty() }
            ?.find { it.productId == BASIC_SUBSCRIPTION }
            ?.subscriptionOfferDetails
            ?.map { it.basePlanId }
            ?.filter { authRepository.getFeatures(it).isEmpty() }
            ?.forEach { basePlanId ->
                val features = subscriptionsService.features(basePlanId).features
                logcat { "Subscription features for base plan $basePlanId fetched: $features" }
                if (features.isNotEmpty()) {
                    authRepository.setFeatures(basePlanId, features.toSet())
                }
            }
    }
}
