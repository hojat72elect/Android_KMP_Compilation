

package com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.ContributesActivePlugin
import com.duckduckgo.common.ui.view.InfoPanel.Companion.APPTP_SETTINGS_ANNOTATION
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.mobile.android.vpn.R
import com.duckduckgo.mobile.android.vpn.apps.ui.ManageRecentAppsProtectionActivity
import com.duckduckgo.mobile.android.vpn.pixels.DeviceShieldPixels
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnRunningState.ENABLED
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnState
import com.duckduckgo.mobile.android.vpn.ui.onboarding.VpnStore
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message.AppTPStateMessagePlugin.Companion.PRIORITY_NEXT_SESSION
import com.duckduckgo.mobile.android.vpn.ui.tracker_activity.view.message.AppTPStateMessagePlugin.DefaultAppTPMessageAction
import javax.inject.Inject

@ContributesActivePlugin(
    scope = AppScope::class,
    boundType = AppTPStateMessagePlugin::class,
    priority = PRIORITY_NEXT_SESSION,
)
class NextSessionEnabledMessagePlugin @Inject constructor(
    private val vpnStore: VpnStore,
    private val deviceShieldPixels: DeviceShieldPixels,
) : AppTPStateMessagePlugin {
    override fun getView(
        context: Context,
        vpnState: VpnState,
        clickListener: (DefaultAppTPMessageAction) -> Unit,
    ): View? {
        return if (vpnState.state == ENABLED && !vpnStore.getAndSetOnboardingSession()) {
            AppTpEnabledInfoPanel(context).apply {
                setClickableLink(
                    APPTP_SETTINGS_ANNOTATION,
                    context.getText(R.string.atp_ActivityEnabledMoreThanADayLabel),
                ) { context.launchManageAppsProtection() }
            }
        } else {
            null
        }
    }

    private fun Context.launchManageAppsProtection() {
        deviceShieldPixels.didOpenManageRecentAppSettings()
        startActivity(ManageRecentAppsProtectionActivity.intent(this))
    }
}
