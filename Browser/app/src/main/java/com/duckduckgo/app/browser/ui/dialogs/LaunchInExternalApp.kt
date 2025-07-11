

package com.duckduckgo.app.browser.ui.dialogs

import androidx.annotation.StringRes
import com.duckduckgo.app.browser.R

enum class LaunchInExternalAppOptions(val order: Int, @StringRes val stringRes: Int) {
    OPEN(0, R.string.open),
    CLOSE_TAB(1, R.string.closeTab),
    CANCEL(2, R.string.cancel),
    ;

    companion object {
        fun asOptions(): List<Int> {
            return listOf(OPEN.stringRes, CLOSE_TAB.stringRes, CANCEL.stringRes)
        }
        fun getOptionFromPosition(position: Int): LaunchInExternalAppOptions {
            var defaultOption = OPEN
            values().forEach {
                if (it.order == position) {
                    defaultOption = it
                }
            }
            return defaultOption
        }
    }
}
