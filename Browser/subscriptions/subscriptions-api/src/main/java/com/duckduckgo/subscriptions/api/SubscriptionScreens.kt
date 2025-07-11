

package com.duckduckgo.subscriptions.api

import com.duckduckgo.navigation.api.GlobalActivityStarter.ActivityParams

sealed class SubscriptionScreens {
    data object SubscriptionScreenNoParams : ActivityParams
}
