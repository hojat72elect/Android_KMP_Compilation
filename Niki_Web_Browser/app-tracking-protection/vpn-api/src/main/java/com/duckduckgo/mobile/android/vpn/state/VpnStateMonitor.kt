

package com.duckduckgo.mobile.android.vpn.state

import com.duckduckgo.mobile.android.vpn.VpnFeature
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.AlwaysOnState.Companion.DEFAULT
import kotlinx.coroutines.flow.Flow

interface VpnStateMonitor {

    /**
     * Returns a flow of VPN changes for the given [vpnFeature]
     * It follows the following truth table:
     * * when the VPN is disabled the flow will emit a [VpnState.DISABLED]
     * * else it will return the state of the feature
     */
    fun getStateFlow(vpnFeature: VpnFeature): Flow<VpnState>

    /**
     * @return `true` if always-on is enabled for our VPN profile, `false` otherwise.
     */
    suspend fun isAlwaysOnEnabled(): Boolean

    /**
     * @return `true` if the VPNService has been unexpectedly disabled OR killed by the Android system, `false` otherwise.
     */
    suspend fun vpnLastDisabledByAndroid(): Boolean

    data class VpnState(
        val state: VpnRunningState,
        val stopReason: VpnStopReason? = null,
        val alwaysOnState: AlwaysOnState = DEFAULT,
    )

    data class AlwaysOnState(val enabled: Boolean, val lockedDown: Boolean) {
        companion object {
            val DEFAULT = AlwaysOnState(enabled = false, lockedDown = false)
            val ALWAYS_ON_ENABLED = AlwaysOnState(enabled = true, lockedDown = false)
            val ALWAYS_ON_LOCKED_DOWN = AlwaysOnState(enabled = true, lockedDown = true)
        }

        fun isAlwaysOnLockedDown(): Boolean = enabled && lockedDown
    }

    sealed class VpnRunningState {
        data object ENABLING : VpnRunningState()
        data object ENABLED : VpnRunningState()
        data object DISABLED : VpnRunningState()
        data object INVALID : VpnRunningState()
    }

    sealed class VpnStopReason {
        data class SELF_STOP(val snoozedTriggerAtMillis: Long = 0L) : VpnStopReason()
        data object ERROR : VpnStopReason()
        data object REVOKED : VpnStopReason()
        data object UNKNOWN : VpnStopReason()
        data object RESTART : VpnStopReason()
    }
}
