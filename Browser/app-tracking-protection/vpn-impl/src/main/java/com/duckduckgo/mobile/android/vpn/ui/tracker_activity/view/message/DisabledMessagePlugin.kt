

package com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.ContributesActivePlugin
import com.duckduckgo.common.ui.view.InfoPanel.Companion.REPORT_ISSUES_ANNOTATION
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.R
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnRunningState.DISABLED
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnState
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnStopReason.SELF_STOP
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message.AppTPStateMessagePlugin.Companion.PRIORITY_DISABLED
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message.AppTPStateMessagePlugin.DefaultAppTPMessageAction
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message.AppTPStateMessagePlugin.DefaultAppTPMessageAction.LaunchFeedback
import javax.inject.Inject

@ContributesActivePlugin(
    scope = AppScope::class,
    boundType = AppTPStateMessagePlugin::class,
    priority = PRIORITY_DISABLED,
)
class DisabledMessagePlugin @Inject constructor() : AppTPStateMessagePlugin {
    override fun getView(
        context: Context,
        vpnState: VpnState,
        clickListener: (DefaultAppTPMessageAction) -> Unit,
    ): View? {
        return if (vpnState.state == DISABLED && vpnState.stopReason is SELF_STOP) {
            AppTpDisabledInfoPanel(context).apply {
                setClickableLink(
                    REPORT_ISSUES_ANNOTATION,
                    context.getText(R.string.atp_ActivityDisabledLabel),
                ) { clickListener.invoke(LaunchFeedback) }
            }
        } else {
            null
        }
    }
}
