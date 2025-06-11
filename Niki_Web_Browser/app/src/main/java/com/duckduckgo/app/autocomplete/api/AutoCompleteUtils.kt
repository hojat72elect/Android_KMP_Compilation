

package com.duckduckgo.app.autocomplete.api

import android.net.Uri

fun Uri.isRoot(): Boolean {
    return (path.isNullOrEmpty() || path == "/") &&
        query == null &&
        fragment == null &&
        userInfo == null
}

fun String.tokensFrom(): List<String> {
    return this
        .split(Regex("\\s+"))
        .filter { it.isNotEmpty() }
        .map { it.lowercase() }
}

const val DEFAULT_SCORE = -1
