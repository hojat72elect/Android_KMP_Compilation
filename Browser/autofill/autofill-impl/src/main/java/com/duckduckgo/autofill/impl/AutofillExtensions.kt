

package com.duckduckgo.autofill.impl

import com.duckduckgo.autofill.api.AutofillScreenLaunchSource
import com.duckduckgo.autofill.api.AutofillScreenLaunchSource.AutofillSettings
import com.duckduckgo.autofill.api.AutofillScreenLaunchSource.BrowserOverflow
import com.duckduckgo.autofill.api.AutofillScreenLaunchSource.BrowserSnackbar
import com.duckduckgo.autofill.api.AutofillScreenLaunchSource.DisableInSettingsPrompt
import com.duckduckgo.autofill.api.AutofillScreenLaunchSource.InternalDevSettings
import com.duckduckgo.autofill.api.AutofillScreenLaunchSource.NewTabShortcut
import com.duckduckgo.autofill.api.AutofillScreenLaunchSource.SettingsActivity
import com.duckduckgo.autofill.api.AutofillScreenLaunchSource.Sync
import com.duckduckgo.autofill.api.AutofillScreenLaunchSource.Unknown

fun AutofillScreenLaunchSource.asString(): String {
    return when (this) {
        SettingsActivity -> "settings"
        BrowserOverflow -> "overflow_menu"
        Sync -> "sync"
        DisableInSettingsPrompt -> "save_login_disable_prompt"
        NewTabShortcut -> "new_tab_page_shortcut"
        BrowserSnackbar -> "browser_snackbar"
        InternalDevSettings -> "internal_dev_settings"
        Unknown -> "unknown"
        AutofillSettings -> "autofill_settings"
    }
}
