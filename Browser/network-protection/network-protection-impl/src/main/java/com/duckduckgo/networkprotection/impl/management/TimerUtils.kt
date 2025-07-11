

package com.duckduckgo.networkprotection.impl.management

import java.util.concurrent.TimeUnit

internal fun Long.toDisplayableTimerText(): String {
    val hours = TimeUnit.MILLISECONDS.toHours(this) % 60
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
