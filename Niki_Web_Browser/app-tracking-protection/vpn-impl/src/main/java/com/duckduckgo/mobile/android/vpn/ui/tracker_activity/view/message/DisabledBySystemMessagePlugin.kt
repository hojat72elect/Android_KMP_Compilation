

package com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.ContributesActivePlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.R
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnRunningState.DISABLED
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnState
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message.AppTPStateMessagePlugin.Companion.PRIORITY_DISABLED_BY_SYSTEM
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message.AppTPStateMessagePlugin.DefaultAppTPMessageAction
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message.AppTPStateMessagePlugin.DefaultAppTPMessageAction.ReenableAppTP
import javax.inject.Inject

@ContributesActivePlugin(
    scope = AppScope::class,
    boundType = AppTPStateMessagePlugin::class,
    priority = PRIORITY_DISABLED_BY_SYSTEM,
)
class DisabledBySystemMessagePlugin @Inject constructor() : AppTPStateMessagePlugin {
    override fun getView(
        context: Context,
        vpnState: VpnState,
        clickListener: (DefaultAppTPMessageAction) -> Unit,
    ): View? {
        return if (vpnState.state == DISABLED) {
            AppTpDisabledInfoPanel(context).apply {
                setClickableLink(
                    RE_ENABLE_ANNOTATION,
                    context.getText(R.string.atp_ActivityDisabledBySystemLabel),
                ) { clickListener.invoke(ReenableAppTP) }
            }
        } else {
            null
        }
    }

    companion object {
        private const val RE_ENABLE_ANNOTATION = "re_enable_link"
    }
}
