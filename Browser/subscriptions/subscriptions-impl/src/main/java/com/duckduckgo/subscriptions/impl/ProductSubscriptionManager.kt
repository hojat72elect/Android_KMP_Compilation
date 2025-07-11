

package com.duckduckgo.subscriptions.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.subscriptions.api.Product
import com.duckduckgo.subscriptions.api.SubscriptionStatus
import com.duckduckgo.subscriptions.api.Subscriptions
import com.duckduckgo.subscriptions.impl.ProductSubscriptionManager.ProductStatus
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ProductSubscriptionManager {

    fun entitlementStatus(vararg products: Product): Flow<ProductStatus>

    enum class ProductStatus {
        ACTIVE,
        EXPIRED,
        SIGNED_OUT,
        INACTIVE,
        WAITING,
        INELIGIBLE,
    }
}

@ContributesBinding(AppScope::class)
class RealProductSubscriptionManager @Inject constructor(
    private val subscriptions: Subscriptions,
) : ProductSubscriptionManager {

    override fun entitlementStatus(vararg products: Product): Flow<ProductStatus> =
        hasEntitlement(*products).map { getEntitlementStatusInternal(it) }

    private fun hasEntitlement(vararg products: Product): Flow<Boolean> =
        subscriptions.getEntitlementStatus().map { entitledProducts -> entitledProducts.any { products.contains(it) } }

    private suspend fun getEntitlementStatusInternal(hasValidEntitlement: Boolean): ProductStatus = when {
        !hasValidEntitlement -> ProductStatus.INELIGIBLE
        else -> when (subscriptions.getSubscriptionStatus()) {
            SubscriptionStatus.INACTIVE, SubscriptionStatus.EXPIRED -> ProductStatus.EXPIRED
            SubscriptionStatus.UNKNOWN -> ProductStatus.SIGNED_OUT
            SubscriptionStatus.AUTO_RENEWABLE, SubscriptionStatus.NOT_AUTO_RENEWABLE, SubscriptionStatus.GRACE_PERIOD -> ProductStatus.ACTIVE
            SubscriptionStatus.WAITING -> ProductStatus.WAITING
        }
    }
}
