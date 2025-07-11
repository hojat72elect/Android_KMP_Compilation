
package com.duckduckgo.vpn.network.impl.models

class Packet {
    var time: Long = 0
    var version = 0
    var protocol = 0
    var flags: String? = null
    var saddr: String? = null
    var sport = 0
    var daddr: String? = null
    var dport = 0
    var data: String? = null
    var uid = 0
    var allowed = false

    override fun toString(): String {
        return "uid=$uid v$version p$protocol $daddr/$dport"
    }
}
