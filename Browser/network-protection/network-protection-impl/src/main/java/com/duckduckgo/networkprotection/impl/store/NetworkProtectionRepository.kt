

package com.duckduckgo.networkprotection.impl.store

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.networkprotection.impl.state.NetPFeatureRemover
import com.duckduckgo.networkprotection.store.NetworkProtectionPrefs
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

interface NetworkProtectionRepository {
    var enabledTimeInMillis: Long
}

@ContributesBinding(
    scope = AppScope::class,
    boundType = NetworkProtectionRepository::class,
)
@ContributesMultibinding(
    scope = AppScope::class,
    boundType = NetPFeatureRemover.NetPStoreRemovalPlugin::class,
)
class RealNetworkProtectionRepository @Inject constructor(
    private val networkProtectionPrefs: NetworkProtectionPrefs,
) : NetworkProtectionRepository, NetPFeatureRemover.NetPStoreRemovalPlugin {

    override var enabledTimeInMillis: Long
        get() = networkProtectionPrefs.getLong(KEY_WG_SERVER_ENABLE_TIME, -1)
        set(value) {
            networkProtectionPrefs.putLong(KEY_WG_SERVER_ENABLE_TIME, value)
        }

    override fun clearStore() {
        networkProtectionPrefs.clear()
    }

    companion object {
        private const val KEY_WG_SERVER_ENABLE_TIME = "wg_server_enable_time"
    }
}
