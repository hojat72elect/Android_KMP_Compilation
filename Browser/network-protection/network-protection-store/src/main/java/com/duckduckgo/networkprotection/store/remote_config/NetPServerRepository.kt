

package com.duckduckgo.networkprotection.store.remote_config

class NetPServerRepository constructor(
    private val serversDao: NetPServersDao,
) {
    suspend fun getServerNames(): Set<String> {
        return serversDao.getServers().map { it.name }.toSet()
    }

    suspend fun getSelectedServer(): NetPEgressServer? {
        return serversDao.getSelectedServer()?.egressServer
    }

    suspend fun setSelectedServer(name: String?) {
        if (name.isNullOrBlank()) {
            serversDao.clearSelectedServer()
        } else {
            serversDao.selectServer(name)
        }
    }

    suspend fun storeServers(servers: List<NetPEgressServer>) {
        serversDao.clearAll()
        serversDao.insertAll(servers)
        serversDao.getSelectedServer()?.egressServerName?.name?.let { selectedServer ->
            // clear selected server if not in the list anymore
            servers.map { it.name }.firstOrNull { it == selectedServer } ?: serversDao.clearSelectedServer()
        }
    }
}
