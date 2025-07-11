

package com.duckduckgo.mobile.android.vpn.service

import java.net.Socket

interface VpnSocketProtector {
    /**
     * Call this method to protect the socket from VPN.
     *
     * @param socket The file descriptor of the socket to protect.
     * @#return true if the socket is protected, false otherwise.
     */
    fun protect(socket: Int): Boolean

    /**
     * Call this method to protect the socket from VPN.
     *
     * @param socket The [Socket] to protect.
     * @#return true if the socket is protected, false otherwise.
     */
    fun protect(socket: Socket): Boolean
}
