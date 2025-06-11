

package com.duckduckgo.common.utils.extensions

import okhttp3.Headers

fun Headers.extractETag(): String {
    return this["eTag"]?.removePrefix("W/")?.removeSurrounding("\"", "\"").orEmpty()
}
