

package com.duckduckgo.networkprotection.internal.settings

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.common.ui.view.divider.HorizontalDivider
import com.duckduckgo.common.ui.view.listitem.SectionHeaderListItem
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.networkprotection.impl.settings.VpnSettingPlugin
import com.duckduckgo.networkprotection.internal.feature.INTERNAL_SETTING_HEADING
import com.duckduckgo.networkprotection.internal.feature.INTERNAL_SETTING_SEPARATOR
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(ActivityScope::class)
@PriorityKey(INTERNAL_SETTING_HEADING)
class VpnInternalSettingHeading @Inject constructor() : VpnSettingPlugin {
    override fun getView(context: Context): View {
        return SectionHeaderListItem(context).apply {
            primaryText = "Employee only settings"
        }
    }
}

@ContributesMultibinding(ActivityScope::class)
@PriorityKey(INTERNAL_SETTING_SEPARATOR)
class VpnInternalSettingSeparator @Inject constructor() : VpnSettingPlugin {
    override fun getView(context: Context): View {
        return HorizontalDivider(context)
    }
}
