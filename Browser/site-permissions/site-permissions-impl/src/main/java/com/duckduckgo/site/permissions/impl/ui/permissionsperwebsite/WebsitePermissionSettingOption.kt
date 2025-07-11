

package com.duckduckgo.site.permissions.impl.ui.permissionsperwebsite

import androidx.annotation.StringRes
import com.duckduckgo.site.permissions.impl.R
import com.duckduckgo.site.permissions.store.sitepermissions.SitePermissionAskSettingType
import java.io.Serializable

data class WebsitePermissionSetting(
    val icon: Int,
    val title: Int,
    val setting: WebsitePermissionSettingOption,
) : Serializable

enum class WebsitePermissionSettingOption(
    val order: Int,
    @StringRes val stringRes: Int,
) {
    ASK(1, R.string.permissionsPerWebsiteAskSetting),
    ASK_DISABLED(1, R.string.permissionsPerWebsiteAskDisabledSetting),
    DENY(2, R.string.permissionsPerWebsiteDenySetting),
    ALLOW(3, R.string.permissionsPerWebsiteAllowSetting),
    ;

    fun toSitePermissionSettingEntityType(): SitePermissionAskSettingType =
        when (this) {
            ASK, ASK_DISABLED -> SitePermissionAskSettingType.ASK_EVERY_TIME
            ALLOW -> SitePermissionAskSettingType.ALLOW_ALWAYS
            DENY -> SitePermissionAskSettingType.DENY_ALWAYS
        }

    companion object {
        fun mapToWebsitePermissionSetting(askSettingType: String?): WebsitePermissionSettingOption =
            when (askSettingType) {
                SitePermissionAskSettingType.ALLOW_ALWAYS.name -> ALLOW
                SitePermissionAskSettingType.DENY_ALWAYS.name -> DENY
                else -> ASK
            }

        fun Int.getPermissionSettingOptionFromPosition(): WebsitePermissionSettingOption {
            return entries.first { it.order == this }
        }
    }
}
