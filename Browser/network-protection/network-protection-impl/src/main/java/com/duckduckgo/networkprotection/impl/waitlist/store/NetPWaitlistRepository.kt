

package com.duckduckgo.networkprotection.impl.waitlist.store

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.networkprotection.impl.state.NetPFeatureRemover
import com.duckduckgo.networkprotection.store.NetpDataStore
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = NetPFeatureRemover.NetPStoreRemovalPlugin::class,
)
class RealNetPWaitlistRepository @Inject constructor(
    private val dataStore: NetpDataStore,
) : NetPFeatureRemover.NetPStoreRemovalPlugin {

    override fun clearStore() {
        dataStore.clear()
    }
}
