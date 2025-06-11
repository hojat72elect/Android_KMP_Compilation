

package com.duckduckgo.networkprotection.impl.configuration

import com.wireguard.config.Config

/**
 * View of the WG config with only relevant information about the egress server
 */
internal data class ServerDetails(
    val serverName: String?,
    val ipAddress: String?,
    val location: String?,
)

internal fun Config.asServerDetails(): ServerDetails {
    return ServerDetails(
        serverName = this.peers[0].name,
        ipAddress = this.peers[0].endpoint?.getResolved()?.host,
        location = this.peers[0].location,
    )
}
