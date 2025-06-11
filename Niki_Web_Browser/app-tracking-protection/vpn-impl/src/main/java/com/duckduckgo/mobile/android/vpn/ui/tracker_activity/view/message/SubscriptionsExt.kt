

package com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message

import com.duckduckgo.subscriptions.api.Subscriptions

suspend fun Subscriptions.isUpsellEligible(): Boolean {
    return !isSignedIn() && isEligible()
}
