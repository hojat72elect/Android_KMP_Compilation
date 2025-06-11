

package com.duckduckgo.mobile.android.vpn.network

import android.content.Context
import com.duckduckgo.common.utils.extensions.getPrivateDnsServerName
import com.duckduckgo.di.scopes.VpnScope
import com.duckduckgo.mobile.android.vpn.network.util.getSystemActiveNetworkDefaultDns
import com.duckduckgo.mobile.android.vpn.network.util.getSystemActiveNetworkSearchDomain
import com.squareup.anvil.annotations.ContributesBinding
import java.net.InetAddress
import javax.inject.Inject

@ContributesBinding(VpnScope::class)
class DnsProviderImpl @Inject constructor(
    private val context: Context,
) : DnsProvider {
    override fun getSystemDns(): List<InetAddress> {
        return runCatching { context.getSystemActiveNetworkDefaultDns() }.getOrDefault(emptyList())
            .map { InetAddress.getByName(it) }
    }

    override fun getSearchDomains(): String? {
        return runCatching { context.getSystemActiveNetworkSearchDomain() }.getOrNull()
    }

    override fun getPrivateDns(): List<InetAddress> {
        return runCatching {
            context.getPrivateDnsServerName()?.let { InetAddress.getAllByName(it).toList() } ?: emptyList()
        }.getOrDefault(emptyList())
    }
}
