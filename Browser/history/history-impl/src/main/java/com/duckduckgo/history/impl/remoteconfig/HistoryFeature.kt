

package com.duckduckgo.history.impl.remoteconfig

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface HistoryFeature {
    val shouldStoreHistory: Boolean
}

@ContributesBinding(AppScope::class)
class RealHistoryFeature @Inject constructor(
    private val historyRemoteFeature: HistoryRemoteFeature,
) : HistoryFeature {
    override val shouldStoreHistory by lazy {
        historyRemoteFeature.self().isEnabled() && historyRemoteFeature.storeHistory().isEnabled()
    }
}
