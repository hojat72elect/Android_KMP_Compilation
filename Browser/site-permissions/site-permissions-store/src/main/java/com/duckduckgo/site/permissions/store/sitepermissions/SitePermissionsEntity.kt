

package com.duckduckgo.site.permissions.store.sitepermissions

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duckduckgo.site.permissions.store.sitepermissions.SitePermissionAskSettingType.ASK_EVERY_TIME

@Entity(tableName = "site_permissions")
data class SitePermissionsEntity(
    @PrimaryKey val domain: String,
    val askCameraSetting: String = ASK_EVERY_TIME.name,
    val askMicSetting: String = ASK_EVERY_TIME.name,
    val askDrmSetting: String = ASK_EVERY_TIME.name,
    val askLocationSetting: String = ASK_EVERY_TIME.name,
)

enum class SitePermissionAskSettingType {
    ASK_EVERY_TIME,
    DENY_ALWAYS,
    ALLOW_ALWAYS,
}
