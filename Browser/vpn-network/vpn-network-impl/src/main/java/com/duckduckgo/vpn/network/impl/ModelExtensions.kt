

package com.duckduckgo.vpn.network.impl

import com.duckduckgo.vpn.network.api.AddressRR
import com.duckduckgo.vpn.network.api.DnsRR
import com.duckduckgo.vpn.network.impl.models.Packet
import com.duckduckgo.vpn.network.impl.models.ResourceRecord

internal fun ResourceRecord.toDnsRR(): DnsRR {
    return DnsRR(Time, QName.orEmpty(), AName.orEmpty(), Resource.orEmpty(), TTL)
}

internal fun Packet.toAddressRR(): AddressRR {
    return AddressRR(daddr.orEmpty(), uid)
}
