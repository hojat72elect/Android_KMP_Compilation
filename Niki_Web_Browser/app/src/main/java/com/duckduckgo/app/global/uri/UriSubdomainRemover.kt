

package com.duckduckgo.app.global.uri

import android.net.Uri

fun Uri.removeSubdomain(): String? {
    val host = host ?: return null

    val splitDomains = host.split(".")
    if (splitDomains.isEmpty()) return null

    val newHost = removeFirstSubdomain(splitDomains)
    if (!isValid(newHost)) return null

    return appendScheme(newHost)
}

private fun removeFirstSubdomain(splitDomains: List<String>): String {
    return splitDomains
        .drop(1)
        .joinToString(".")
}

private fun isValid(uri: String): Boolean {
    if (!uri.contains(".")) {
        return false
    }
    return true
}

private fun Uri.appendScheme(newHost: String): String? {
    return if (scheme == null) {
        newHost
    } else {
        "$scheme://$newHost"
    }
}
