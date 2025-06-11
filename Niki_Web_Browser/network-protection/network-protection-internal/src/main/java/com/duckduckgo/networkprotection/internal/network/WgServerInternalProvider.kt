

package com.duckduckgo.networkprotection.internal.network

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.networkprotection.impl.configuration.Server
import com.duckduckgo.networkprotection.impl.configuration.WgServerDebugProvider
import com.duckduckgo.networkprotection.store.remote_config.NetPEgressServer
import com.duckduckgo.networkprotection.store.remote_config.NetPServerRepository
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(
    scope = AppScope::class,
    rank = ContributesBinding.RANK_HIGHEST, // binding for internal build wins
)
class WgServerInternalProvider @Inject constructor(
    private val netPServerRepository: NetPServerRepository,
) : WgServerDebugProvider {
    override suspend fun getSelectedServerName(): String? {
        return netPServerRepository.getSelectedServer()?.name
    }

    override suspend fun clearSelectedServerName() {
        netPServerRepository.setSelectedServer(null)
    }

    override suspend fun cacheServers(servers: List<Server>) {
        servers.map { server ->
            NetPEgressServer(
                name = server.name,
                publicKey = server.publicKey,
                port = server.port,
                hostnames = server.hostnames,
                ips = server.ips,
                countryCode = server.attributes["country"] as String,
                city = server.attributes["city"] as String,
            )
        }.let {
            netPServerRepository.storeServers(it)
        }
    }
}
