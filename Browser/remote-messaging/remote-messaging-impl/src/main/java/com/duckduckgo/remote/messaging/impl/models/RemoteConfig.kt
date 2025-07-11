

package com.duckduckgo.remote.messaging.impl.models

import com.duckduckgo.remote.messaging.api.MatchingAttribute
import com.duckduckgo.remote.messaging.api.RemoteMessage

data class RemoteConfig(
    val messages: List<RemoteMessage>,
    val rules: List<Rule>,
)

data class Rule(
    val id: Int,
    val targetPercentile: TargetPercentile?,
    val attributes: List<MatchingAttribute>,
)

data class TargetPercentile(
    val before: Float,
)
