

package com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.ContributesActivePlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.R
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnRunningState.ENABLED
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnState
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message.AppTPStateMessagePlugin.Companion.PRIORITY_ACTION_REQUIRED
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message.AppTPStateMessagePlugin.DefaultAppTPMessageAction
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message.AppTPStateMessagePlugin.DefaultAppTPMessageAction.HandleAlwaysOnActionRequired
import javax.inject.Inject

@ContributesActivePlugin(
    scope = AppScope::class,
    boundType = AppTPStateMessagePlugin::class,
    priority = PRIORITY_ACTION_REQUIRED,
)
class ActionRequiredDisabledMessagePlugin @Inject constructor() : AppTPStateMessagePlugin {
    override fun getView(
        context: Context,
        vpnState: VpnState,
        clickListener: (DefaultAppTPMessageAction) -> Unit,
    ): View? {
        return if (vpnState.state == ENABLED && vpnState.alwaysOnState.isAlwaysOnLockedDown()) {
            AppTpDisabledInfoPanel(context).apply {
                setClickableLink(
                    OPEN_SETTINGS_ANNOTATION,
                    context.getText(R.string.atp_AlwaysOnLockDownEnabled),
                ) { clickListener.invoke(HandleAlwaysOnActionRequired) }
            }
        } else {
            null
        }
    }

    companion object {
        private const val OPEN_SETTINGS_ANNOTATION = "open_settings_link"
    }
}
