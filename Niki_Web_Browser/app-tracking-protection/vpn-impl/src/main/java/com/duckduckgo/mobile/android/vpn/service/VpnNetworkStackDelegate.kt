

package com.duckduckgo.mobile.android.vpn.service

import com.duckduckgo.mobile.android.vpn.network.VpnNetworkStack
import kotlin.reflect.KProperty

class VpnNetworkStackDelegate constructor(
    networkStack: VpnNetworkStack? = null,
    private val provider: () -> VpnNetworkStack,
) {

    private var vpnNetworkStack: VpnNetworkStack? = networkStack

    operator fun getValue(thisRef: TrackerBlockingVpnService, property: KProperty<*>): VpnNetworkStack {
        if (vpnNetworkStack == null) {
            vpnNetworkStack = provider.invoke()
        }

        return vpnNetworkStack!!
    }

    operator fun setValue(thisRef: TrackerBlockingVpnService, property: KProperty<*>, value: Any?) {
        if (value is VpnNetworkStack) {
            vpnNetworkStack = value
        }
    }
}
