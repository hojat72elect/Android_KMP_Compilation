

package com.duckduckgo.mobile.android.vpn.network

import java.net.InetAddress

interface DnsProvider {
    /**
     * @return Returns the list of default system DNS configured in the active network
     */
    fun getSystemDns(): List<InetAddress>

    /**
     * @return comma separated domains to search when resolving host names on this link or null
     */
    fun getSearchDomains(): String? = null

    /**
     * @return Returns the list of private DNS set by the user via the Android Private DNS settings, or empty
     * if not set
     */
    fun getPrivateDns(): List<InetAddress>
}
