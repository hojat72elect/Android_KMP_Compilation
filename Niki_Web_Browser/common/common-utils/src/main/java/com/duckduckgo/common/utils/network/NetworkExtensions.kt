

package com.duckduckgo.common.utils.network

import java.net.Inet4Address
import java.net.InetAddress

fun InetAddress.isCGNATed(): Boolean {
    if (this !is Inet4Address) return false

    // CGNAT is 100.64.0.0 -> 100.127.255.255
    val firstOctet = address[0].toUInt()
    val secondOctet = address[1].toUInt()

    return firstOctet == 100u && (secondOctet in 64u..127u)
}
