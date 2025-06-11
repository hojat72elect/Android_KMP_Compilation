

package com.duckduckgo.vpn.internal.feature.bugreport

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollector
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext
import logcat.logcat

interface VpnBugReporter {
    suspend fun generateBugReport(): String
}

@ContributesBinding(ActivityScope::class)
class RealVpnBugReporter @Inject constructor(
    private val vpnStateCollector: VpnStateCollector,
    private val dispatcherProvider: DispatcherProvider,
) : VpnBugReporter {
    override suspend fun generateBugReport(): String {
        return withContext(dispatcherProvider.io()) {
            val state = vpnStateCollector.collectVpnState()
            val bugreport = state.toString(2)
            logcat { "AppTP bugreport generated: $bugreport" }

            bugreport
        }
    }
}
