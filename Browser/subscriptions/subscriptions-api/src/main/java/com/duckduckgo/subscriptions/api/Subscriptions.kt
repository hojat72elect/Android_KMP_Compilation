

package com.duckduckgo.subscriptions.api

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface Subscriptions {

    /**
     * Checks if the user is currently signed in.
     *
     * Note: A signed-in user does not necessarily have an active subscription.
     *
     * @return `true` if the user is signed in; `false` otherwise
     */
    suspend fun isSignedIn(): Boolean

    /**
     * This method returns a [String] with the access token for the authenticated user or [null] if it doesn't exist
     * or any errors arise.
     * @return [String]
     */
    suspend fun getAccessToken(): String?

    /**
     * This method returns a [true] if a  given [product] can be found in the entitlements list or [false] otherwise
     * @return [Boolean]
     */
    fun getEntitlementStatus(): Flow<List<Product>>

    /**
     * @return `true` if the Privacy Pro product is available for the user, `false` otherwise
     */
    suspend fun isEligible(): Boolean

    /**
     * @return `SubscriptionStatus` with the current subscription status
     */
    suspend fun getSubscriptionStatus(): SubscriptionStatus

    /**
     * This is a suspend function because we access disk IO
     * You DO NOT need to set any dispatcher to call this suspend function
     *
     * @return a [Set<Product>] of available products for the subscription or an empty set if subscription is not available
     */
    suspend fun getAvailableProducts(): Set<Product>

    /**
     * @return `true` if the given URL can be handled internally or `false` otherwise
     */
    fun shouldLaunchPrivacyProForUrl(url: String): Boolean

    /**
     * Launches Privacy Pro with Settings as the parent activity
     */
    fun launchPrivacyPro(context: Context, uri: Uri?)

    /**
     * @return `true` if the given Uri leads to the Privacy Pro page, or `false` otherwise
     */
    fun isPrivacyProUrl(uri: Uri): Boolean
}

enum class Product(val value: String) {
    NetP("Network Protection"),
    ITR("Identity Theft Restoration"),
    ROW_ITR("Global Identity Theft Restoration"),
    PIR("Data Broker Protection"),
}

enum class SubscriptionStatus(val statusName: String) {
    AUTO_RENEWABLE("Auto-Renewable"),
    NOT_AUTO_RENEWABLE("Not Auto-Renewable"),
    GRACE_PERIOD("Grace Period"),
    INACTIVE("Inactive"),
    EXPIRED("Expired"),
    UNKNOWN("Unknown"),
    WAITING("Waiting"),
}

enum class ActiveOfferType {
    TRIAL, UNKNOWN
}
