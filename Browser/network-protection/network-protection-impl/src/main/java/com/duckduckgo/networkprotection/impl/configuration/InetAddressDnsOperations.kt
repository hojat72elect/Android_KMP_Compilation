

package com.duckduckgo.networkprotection.impl.configuration

import java.net.InetAddress

/**
 * The block malware DNS IP address is a <<1 bit-wise operations on the last octet based on the default DNS
 * This method assumes the [InetAddress] passed in as parameter is the default DNS.
 *
 * You should only
 */
internal fun InetAddress.computeBlockMalwareDnsOrSame(): InetAddress {
    return kotlin.runCatching {
        // Perform <<1 operation on the last octet
        // Since byte is signed in Kotlin/Java, we mask it with 0xFF to treat it as unsigned
        val newLastOctet = (address.last().toInt() and 0xFF) shl 1

        val newIPAddress = address
        // Update the last octet in the byte array
        newIPAddress[newIPAddress.size - 1] = (newLastOctet and 0xFF).toByte() // Ensure it stays within byte range

        InetAddress.getByAddress(newIPAddress)
    }.getOrNull() ?: this
}
