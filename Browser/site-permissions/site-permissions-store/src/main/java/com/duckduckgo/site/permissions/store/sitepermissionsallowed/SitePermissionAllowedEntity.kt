

package com.duckduckgo.site.permissions.store.sitepermissionsallowed

import androidx.room.Entity
import kotlin.math.abs

@Entity(
    tableName = "site_permission_allowed",
    primaryKeys = ["domain", "tabId", "permissionAllowed"],
)
data class SitePermissionAllowedEntity(
    val domain: String,
    val tabId: String,
    val permissionAllowed: String,
    val allowedAt: Long,
) {

    fun allowedWithin24h(): Boolean {
        val now = System.currentTimeMillis()
        val diff = abs(now - this.allowedAt) / 3600000
        return diff <= 24
    }
}
