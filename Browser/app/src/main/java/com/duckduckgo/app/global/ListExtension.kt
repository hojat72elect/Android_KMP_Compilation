

package com.duckduckgo.app.global

fun <T : CharSequence> List<T>.filterBlankItems() = filter { it.isNotBlank() }
