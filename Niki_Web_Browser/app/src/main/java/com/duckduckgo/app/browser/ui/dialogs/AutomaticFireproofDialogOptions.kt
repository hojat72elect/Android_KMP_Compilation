

package com.duckduckgo.app.browser.ui.dialogs

import androidx.annotation.StringRes
import com.duckduckgo.app.browser.R

enum class AutomaticFireproofDialogOptions(val order: Int, @StringRes val stringRes: Int) {
    ALWAYS(0, R.string.automaticFireproofWebsiteLoginDialogFirstOption),
    FIREPROOF_THIS_SITE(1, R.string.automaticFireproofWebsiteLoginDialogSecondOption),
    NOT_NOW(2, R.string.automaticFireproofWebsiteLoginDialogThirdOption),
    ;

    companion object {
        fun asOptions(): List<Int> {
            return listOf(ALWAYS.stringRes, FIREPROOF_THIS_SITE.stringRes, NOT_NOW.stringRes)
        }
        fun getOptionFromPosition(position: Int): AutomaticFireproofDialogOptions {
            var defaultOption = ALWAYS
            values().forEach {
                if (it.order == position) {
                    defaultOption = it
                }
            }
            return defaultOption
        }
    }
}
