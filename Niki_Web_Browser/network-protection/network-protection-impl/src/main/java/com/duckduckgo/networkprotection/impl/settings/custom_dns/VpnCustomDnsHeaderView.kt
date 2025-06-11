

package com.duckduckgo.networkprotection.impl.settings.custom_dns

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.common.ui.view.listitem.SectionHeaderListItem
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.networkprotection.impl.R
import com.duckduckgo.networkprotection.impl.settings.CUSTOM_DNS_HEADER_PRIORITY
import com.duckduckgo.networkprotection.impl.settings.VpnSettingPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(ActivityScope::class)
@PriorityKey(CUSTOM_DNS_HEADER_PRIORITY)
class VpnCustomDnsHeaderView @Inject constructor() : VpnSettingPlugin {
    override fun getView(context: Context): View {
        return SectionHeaderListItem(context).apply {
            primaryText = context.getString(R.string.netpSetttingDnsHeader)
        }
    }
}
