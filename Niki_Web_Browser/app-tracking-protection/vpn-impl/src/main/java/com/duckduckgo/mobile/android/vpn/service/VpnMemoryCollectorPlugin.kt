

package com.duckduckgo.mobile.android.vpn.service

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.VpnScope

/**
 * Implement this plugin interface if you want the [TrackerBlockingVpnService] to notify you when it's
 * about to send memory metrics to the backend.
 *
 * The [VpnMemoryCollectorPlugin.collectMemoryMetrics] method will be called to give you the chance
 * to collect and return a map with the metrics that are interesting for you.
 */
@ContributesPluginPoint(VpnScope::class)
interface VpnMemoryCollectorPlugin {
    fun collectMemoryMetrics(): Map<String, String>
}
