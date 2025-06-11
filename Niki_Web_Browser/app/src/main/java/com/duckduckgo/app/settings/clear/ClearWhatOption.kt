

package com.duckduckgo.app.settings.clear

import com.duckduckgo.app.settings.clear.ClearWhatOption.CLEAR_NONE
import com.duckduckgo.app.settings.clear.ClearWhatOption.CLEAR_TABS_AND_DATA
import com.duckduckgo.app.settings.clear.ClearWhatOption.CLEAR_TABS_ONLY

enum class ClearWhatOption {
    CLEAR_NONE,
    CLEAR_TABS_ONLY,
    CLEAR_TABS_AND_DATA,
    ;

    fun getOptionIndex(): Int {
        return when (this) {
            CLEAR_NONE -> 1
            CLEAR_TABS_ONLY -> 2
            CLEAR_TABS_AND_DATA -> 3
        }
    }
}

fun Int.getClearWhatOptionForIndex(): ClearWhatOption {
    return when (this) {
        2 -> CLEAR_TABS_ONLY
        3 -> CLEAR_TABS_AND_DATA
        else -> CLEAR_NONE
    }
}
