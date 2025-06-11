

package com.duckduckgo.mobile.android.vpn

/**
 * Public API to handle start/stop type of actions for the device VPN
 */
interface Vpn {
    /**
     * Enables the device VPN by starting the VPN service
     */
    suspend fun start()

    /**
     * Pauses the VPN tunnel.
     * All features that were registered to use the VPN tunnel (eg. AppTP, NetP) continue to be registered and so a subsequent
     * [start] call will re-enable them all
     */
    suspend fun pause()

    /**
     * Stops the VPN tunnel AND all features registered to use the VPN tunnel (eg. AppTP, NetP). A subsequent call to [start]
     * will not re-start the VPN because no feature would be registered.
     */
    suspend fun stop()

    /**
     * Snoozes the VPN for [triggerAtMillis] milliseconds
     */
    suspend fun snooze(triggerAtMillis: Long)
}
