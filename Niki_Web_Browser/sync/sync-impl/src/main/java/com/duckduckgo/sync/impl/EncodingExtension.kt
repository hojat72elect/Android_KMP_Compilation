

package com.duckduckgo.sync.impl

import android.util.Base64

internal fun String.encodeB64(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)
}

internal fun String.decodeB64(): String {
    return String(Base64.decode(this, Base64.DEFAULT))
}

/**
 * This assumes the string is already base64-encoded
 */
internal fun String.applyUrlSafetyFromB64(): String {
    return this
        .replace('+', '-')
        .replace('/', '_')
        .trimEnd('=')
}

internal fun String.removeUrlSafetyToRestoreB64(): String {
    return this
        .replace('-', '+')
        .replace('_', '/')
        .restoreBase64Padding()
}

private fun String.restoreBase64Padding(): String {
    return when (length % 4) {
        2 -> "$this=="
        3 -> "$this="
        else -> this
    }
}
