

package com.duckduckgo.app.fire.fireproofwebsite.ui

import com.duckduckgo.app.browser.R

enum class AutomaticFireproofSetting(val stringRes: Int) {
    ASK_EVERY_TIME(R.string.fireproofWebsiteSettingsSelectionDialogAskEveryTime),
    ALWAYS(R.string.fireproofWebsiteSettingsSelectionDialogAlways),
    NEVER(R.string.fireproofWebsiteSettingsSelectionDialogNever),
    ;

    fun getOptionIndex(): Int {
        return when (this) {
            ASK_EVERY_TIME -> 1
            ALWAYS -> 2
            NEVER -> 3
        }
    }

    companion object {
        fun Int.getFireproofSettingOptionForIndex(): AutomaticFireproofSetting {
            return when (this) {
                2 -> ALWAYS
                3 -> NEVER
                else -> ASK_EVERY_TIME
            }
        }
    }
}
