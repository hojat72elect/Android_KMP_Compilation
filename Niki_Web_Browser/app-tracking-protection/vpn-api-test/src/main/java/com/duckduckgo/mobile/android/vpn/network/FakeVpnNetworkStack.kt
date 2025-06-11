

package com.duckduckgo.mobile.android.vpn.network

import android.os.ParcelFileDescriptor
import com.duckduckgo.mobile.android.vpn.state.VpnStateMonitor.VpnStopReason

class FakeVpnNetworkStack(override val name: String) : VpnNetworkStack {
    override fun onCreateVpn(): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun onPrepareVpn(): Result<VpnNetworkStack.VpnTunnelConfig> {
        return Result.success(
            VpnNetworkStack.VpnTunnelConfig(
                mtu = 1500,
                addresses = emptyMap(),
                dns = emptySet(),
                searchDomains = null,
                customDns = emptySet(),
                routes = emptyMap(),
                appExclusionList = emptySet(),
            ),
        )
    }

    override fun onStartVpn(tunfd: ParcelFileDescriptor): Result<Unit> {
        return Result.success(Unit)
    }

    override fun onStopVpn(reason: VpnStopReason): Result<Unit> {
        return Result.success(Unit)
    }

    override fun onDestroyVpn(): Result<Unit> {
        return Result.success(Unit)
    }
}
