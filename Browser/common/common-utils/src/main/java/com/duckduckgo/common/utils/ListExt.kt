

package com.duckduckgo.common.utils

fun <T> List<T>.swap(from: Int, to: Int): List<T> = toMutableList().apply {
    val item = this[from]
    removeAt(from)
    add(to, item)
}
