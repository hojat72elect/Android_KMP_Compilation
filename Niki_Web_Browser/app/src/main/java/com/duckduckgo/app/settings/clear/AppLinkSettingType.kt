

package com.duckduckgo.app.settings.clear

enum class AppLinkSettingType {
    ASK_EVERYTIME,
    ALWAYS,
    NEVER,
    ;

    fun getOptionIndex(): Int {
        return when (this) {
            ASK_EVERYTIME -> 1
            ALWAYS -> 2
            NEVER -> 3
        }
    }
}

fun Int.getAppLinkSettingForIndex(): AppLinkSettingType {
    return when (this) {
        2 -> AppLinkSettingType.ALWAYS
        3 -> AppLinkSettingType.NEVER
        else -> AppLinkSettingType.ASK_EVERYTIME
    }
}
