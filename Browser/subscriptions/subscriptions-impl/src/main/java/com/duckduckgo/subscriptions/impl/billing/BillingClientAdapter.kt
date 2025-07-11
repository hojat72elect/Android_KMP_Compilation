

package com.duckduckgo.subscriptions.impl.billing

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchaseHistoryRecord

interface BillingClientAdapter {
    val ready: Boolean

    suspend fun connect(
        purchasesListener: (PurchasesUpdateResult) -> Unit,
        disconnectionListener: () -> Unit,
    ): BillingInitResult

    suspend fun getSubscriptions(productIds: List<String>): SubscriptionsResult

    suspend fun getSubscriptionsPurchaseHistory(): SubscriptionsPurchaseHistoryResult

    suspend fun launchBillingFlow(
        activity: Activity,
        productDetails: ProductDetails,
        offerToken: String,
        externalId: String,
    ): LaunchBillingFlowResult
}

sealed class BillingInitResult {
    data object Success : BillingInitResult()
    data class Failure(val billingError: BillingError) : BillingInitResult()
}

sealed class SubscriptionsResult {
    data class Success(val products: List<ProductDetails>) : SubscriptionsResult()

    data class Failure(
        val billingError: BillingError? = null,
        val debugMessage: String? = null,
    ) : SubscriptionsResult()
}

sealed class SubscriptionsPurchaseHistoryResult {
    data class Success(val history: List<PurchaseHistoryRecord>) : SubscriptionsPurchaseHistoryResult()
    data object Failure : SubscriptionsPurchaseHistoryResult()
}

sealed class LaunchBillingFlowResult {
    data object Success : LaunchBillingFlowResult()
    data object Failure : LaunchBillingFlowResult()
}

sealed class PurchasesUpdateResult {
    data class PurchasePresent(
        val purchaseToken: String,
        val packageName: String,
    ) : PurchasesUpdateResult()

    data object PurchaseAbsent : PurchasesUpdateResult()
    data object UserCancelled : PurchasesUpdateResult()
    data class Failure(val errorType: String) : PurchasesUpdateResult()
}

enum class BillingError {
    SERVICE_TIMEOUT,
    FEATURE_NOT_SUPPORTED,
    SERVICE_DISCONNECTED,
    USER_CANCELED,
    SERVICE_UNAVAILABLE,
    BILLING_UNAVAILABLE,
    ITEM_UNAVAILABLE,
    DEVELOPER_ERROR,
    ERROR,
    ITEM_ALREADY_OWNED,
    ITEM_NOT_OWNED,
    NETWORK_ERROR,
    UNKNOWN_ERROR, // for when billing returns something we don't understand
    BILLING_CRASH_ERROR, // This is our own error
    ;
}
