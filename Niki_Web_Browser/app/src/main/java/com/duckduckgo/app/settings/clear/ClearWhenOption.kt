

package com.duckduckgo.app.settings.clear

import com.duckduckgo.app.settings.clear.ClearWhenOption.APP_EXIT_ONLY
import com.duckduckgo.app.settings.clear.ClearWhenOption.APP_EXIT_OR_15_MINS
import com.duckduckgo.app.settings.clear.ClearWhenOption.APP_EXIT_OR_30_MINS
import com.duckduckgo.app.settings.clear.ClearWhenOption.APP_EXIT_OR_5_MINS
import com.duckduckgo.app.settings.clear.ClearWhenOption.APP_EXIT_OR_5_SECONDS
import com.duckduckgo.app.settings.clear.ClearWhenOption.APP_EXIT_OR_60_MINS

enum class ClearWhenOption {
    APP_EXIT_ONLY,
    APP_EXIT_OR_5_MINS,
    APP_EXIT_OR_15_MINS,
    APP_EXIT_OR_30_MINS,
    APP_EXIT_OR_60_MINS,

    // only available to debug builds
    APP_EXIT_OR_5_SECONDS,
    ;

    fun getOptionIndex(): Int {
        return when (this) {
            APP_EXIT_ONLY -> 1
            APP_EXIT_OR_5_MINS -> 2
            APP_EXIT_OR_15_MINS -> 3
            APP_EXIT_OR_30_MINS -> 4
            APP_EXIT_OR_60_MINS -> 5
            APP_EXIT_OR_5_SECONDS -> 6
        }
    }
}

fun Int.getClearWhenForIndex(): ClearWhenOption {
    return when (this) {
        2 -> APP_EXIT_OR_5_MINS
        3 -> APP_EXIT_OR_15_MINS
        4 -> APP_EXIT_OR_30_MINS
        5 -> APP_EXIT_OR_60_MINS
        6 -> APP_EXIT_OR_5_SECONDS
        else -> APP_EXIT_ONLY
    }
}
