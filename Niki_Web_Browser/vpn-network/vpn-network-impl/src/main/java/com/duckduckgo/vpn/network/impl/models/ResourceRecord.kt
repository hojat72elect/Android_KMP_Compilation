
package com.duckduckgo.vpn.network.impl.models

import java.text.SimpleDateFormat
import java.util.*

class ResourceRecord {
    var Time: Long = 0
    var QName: String? = null
    var AName: String? = null
    var Resource: String? = null
    var TTL = 0

    override fun toString(): String {
        return (
            formatter.format(Date(Time).time) +
                " Q " +
                QName +
                " A " +
                AName +
                " R " +
                Resource +
                " TTL " +
                TTL +
                " " +
                formatter.format(Date(Time + TTL * 1000L).time)
            )
    }

    companion object {
        private val formatter = SimpleDateFormat.getDateTimeInstance()
    }
}
