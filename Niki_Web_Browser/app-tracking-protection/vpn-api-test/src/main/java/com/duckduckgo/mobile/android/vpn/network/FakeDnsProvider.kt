

package com.duckduckgo.mobile.android.vpn.network

import java.net.InetAddress

class FakeDnsProvider : DnsProvider {
    val mutableSystemDns = mutableListOf<InetAddress>()
    val mutablePrivateDns = mutableListOf<InetAddress>()
    var searchDomain: String? = null
    override fun getSystemDns(): List<InetAddress> = mutableSystemDns

    override fun getSearchDomains(): String? = searchDomain

    override fun getPrivateDns(): List<InetAddress> = mutablePrivateDns
}
