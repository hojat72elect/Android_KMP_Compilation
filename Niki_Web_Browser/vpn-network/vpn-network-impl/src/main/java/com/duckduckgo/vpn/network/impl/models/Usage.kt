
package com.duckduckgo.vpn.network.impl.models

import java.text.SimpleDateFormat
import java.util.*

class Usage {
    var Time: Long = 0
    var Version = 0
    var Protocol = 0
    var DAddr: String? = null
    var DPort = 0
    var Uid = 0
    var Sent: Long = 0
    var Received: Long = 0

    override fun toString(): String {
        return (
            formatter.format(Date(Time).time) +
                " v" +
                Version +
                " p" +
                Protocol +
                " " +
                DAddr +
                "/" +
                DPort +
                " uid " +
                Uid +
                " out " +
                Sent +
                " in " +
                Received
            )
    }

    companion object {
        private val formatter = SimpleDateFormat.getDateTimeInstance()
    }
}
