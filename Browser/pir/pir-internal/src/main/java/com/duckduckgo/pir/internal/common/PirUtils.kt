

package com.duckduckgo.pir.internal.common

internal fun <T> List<T>.splitIntoParts(parts: Int): List<List<T>> {
    return if (this.isEmpty()) {
        emptyList()
    } else {
        val chunkSize = (this.size + parts - 1) / parts // Ensure rounding up
        this.chunked(chunkSize)
    }
}
