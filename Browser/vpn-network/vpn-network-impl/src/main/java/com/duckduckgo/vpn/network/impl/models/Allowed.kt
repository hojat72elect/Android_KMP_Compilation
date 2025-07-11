
package com.duckduckgo.vpn.network.impl.models

class Allowed {
    var raddr: String?
    var rport: Int

    constructor() {
        raddr = null
        rport = 0
    }

    constructor(raddr: String?, rport: Int) {
        this.raddr = raddr
        this.rport = rport
    }
}
