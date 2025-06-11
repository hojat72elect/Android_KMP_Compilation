

package com.duckduckgo.user.agent.impl

import com.duckduckgo.user.agent.store.UserAgentFeatureName

fun userAgentFeatureValueOf(value: String): UserAgentFeatureName? {
    return UserAgentFeatureName.values().find { it.value == value }
}
