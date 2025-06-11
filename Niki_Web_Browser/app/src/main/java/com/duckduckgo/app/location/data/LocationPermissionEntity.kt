

package com.duckduckgo.app.location.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationPermissions")
data class LocationPermissionEntity(
    @PrimaryKey val domain: String,
    val permission: LocationPermissionType,
)

private const val HTTPS_HOST_PREFIX = "https://"
private const val WWW_SUFFIX = "/"

fun LocationPermissionEntity.forFireproofing(): String {
    return domain.takeIf { it.startsWith(HTTPS_HOST_PREFIX, ignoreCase = true) && it.endsWith(WWW_SUFFIX, ignoreCase = true) }
        ?.drop(HTTPS_HOST_PREFIX.length)?.dropLast(WWW_SUFFIX.length) ?: domain
}

private const val TYPE_ALLOW_ALWAYS = 1
private const val TYPE_ALLOW_ONCE = 2
private const val TYPE_DENY_ALWAYS = 3
private const val TYPE_DENY_ONCE = 4

enum class LocationPermissionType(val value: Int) {

    ALLOW_ALWAYS(TYPE_ALLOW_ALWAYS),
    ALLOW_ONCE(TYPE_ALLOW_ONCE),
    DENY_ALWAYS(TYPE_DENY_ALWAYS),
    DENY_ONCE(TYPE_DENY_ONCE),
    ;

    companion object {
        private val map = values().associateBy(LocationPermissionType::value)
        fun fromValue(value: Int) = map[value]
    }
}
