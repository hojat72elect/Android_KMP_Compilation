

package com.duckduckgo.networkprotection.impl.quickaccess

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.networkprotection.api.NetworkProtectionAccessState
import com.duckduckgo.networkprotection.api.NetworkProtectionAccessState.NetPAccessState.UnLocked
import com.duckduckgo.networkprotection.api.NetworkProtectionState
import com.duckduckgo.networkprotection.impl.quickaccess.VpnTileStateProvider.VpnTileState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface VpnTileStateProvider {
    suspend fun getVpnTileState(): VpnTileState

    enum class VpnTileState {
        CONNECTED,
        DISCONNECTED,
        UNAVAILABLE,
    }
}

@ContributesBinding(AppScope::class)
class RealVpnTileStateProvider @Inject constructor(
    private val netpAccessState: NetworkProtectionAccessState,
    private val networkProtectionState: NetworkProtectionState,
) : VpnTileStateProvider {
    override suspend fun getVpnTileState(): VpnTileState {
        return netpAccessState.getState().run {
            if (this !is UnLocked) {
                VpnTileState.UNAVAILABLE
            } else {
                if (networkProtectionState.isRunning()) {
                    VpnTileState.CONNECTED
                } else {
                    VpnTileState.DISCONNECTED
                }
            }
        }
    }
}
