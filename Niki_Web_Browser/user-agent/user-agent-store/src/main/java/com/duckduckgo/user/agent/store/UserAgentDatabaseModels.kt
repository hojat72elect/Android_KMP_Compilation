

package com.duckduckgo.user.agent.store

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duckduckgo.feature.toggles.api.FeatureException

@Entity(tableName = "user_agent_exceptions")
data class UserAgentExceptionEntity(
    @PrimaryKey val domain: String,
    val reason: String,
)

fun UserAgentExceptionEntity.toFeatureException(): FeatureException {
    return FeatureException(domain = this.domain, reason = this.reason)
}
