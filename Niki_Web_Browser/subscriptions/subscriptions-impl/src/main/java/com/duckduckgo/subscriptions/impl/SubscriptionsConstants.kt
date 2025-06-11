

package com.duckduckgo.subscriptions.impl

object SubscriptionsConstants {

    // List of subscriptions
    const val BASIC_SUBSCRIPTION = "ddg_privacy_pro"
    val LIST_OF_PRODUCTS = listOf(BASIC_SUBSCRIPTION)

    // List of plans
    const val YEARLY_PLAN_US = "ddg-privacy-pro-yearly-renews-us"
    const val MONTHLY_PLAN_US = "ddg-privacy-pro-monthly-renews-us"
    const val YEARLY_PLAN_ROW = "ddg-privacy-pro-yearly-renews-row"
    const val MONTHLY_PLAN_ROW = "ddg-privacy-pro-monthly-renews-row"

    // List of offers
    const val MONTHLY_FREE_TRIAL_OFFER_US = "ddg-privacy-pro-freetrial-monthly-renews-us"
    const val YEARLY_FREE_TRIAL_OFFER_US = "ddg-privacy-pro-freetrial-yearly-renews-us"

    // List of features
    const val LEGACY_FE_NETP = "vpn"
    const val LEGACY_FE_ITR = "identity-theft-restoration"
    const val LEGACY_FE_PIR = "personal-information-removal"

    const val NETP = "Network Protection"
    const val ITR = "Identity Theft Restoration"
    const val ROW_ITR = "Global Identity Theft Restoration"
    const val PIR = "Data Broker Protection"

    // Platform
    const val PLATFORM = "android"

    // Recurrence
    const val MONTHLY = "Monthly"
    const val YEARLY = "Yearly"

    // URLs
    const val BUY_URL = "https://duckduckgo.com/subscriptions"
    const val ACTIVATE_URL = "https://duckduckgo.com/subscriptions/activation-flow"
    const val ITR_URL = "https://duckduckgo.com/identity-theft-restoration"
    const val FAQS_URL = "https://duckduckgo.com/duckduckgo-help-pages/privacy-pro/"
    const val PRIVACY_PRO_ETLD = "duckduckgo.com"
    const val PRIVACY_PRO_PATH = "pro"
}
