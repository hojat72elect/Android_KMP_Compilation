

// ktlint-disable filename
package com.duckduckgo.request.filterer.store

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duckduckgo.feature.toggles.api.FeatureException

@Entity(tableName = "settings_entity")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val windowInMs: Int,
)

@Entity(tableName = "request_filterer_exceptions")
data class RequestFiltererExceptionEntity(
    @PrimaryKey val domain: String,
    val reason: String,
)

fun RequestFiltererExceptionEntity.toFeatureException(): FeatureException {
    return FeatureException(domain = this.domain, reason = this.reason)
}
