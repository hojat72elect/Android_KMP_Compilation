

package com.duckduckgo.app.browser.defaultbrowsing

import android.content.Intent
import android.provider.Settings
import androidx.core.os.bundleOf

class DefaultBrowserSystemSettings {

    companion object {
        fun intent(): Intent {
            val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
            intent.putExtra(SETTINGS_SELECT_OPTION_KEY, DEFAULT_BROWSER_APP_OPTION)
            intent.putExtra(SETTINGS_SHOW_FRAGMENT_ARGS, bundleOf(SETTINGS_SELECT_OPTION_KEY to DEFAULT_BROWSER_APP_OPTION))
            return intent
        }

        private const val SETTINGS_SELECT_OPTION_KEY = ":settings:fragment_args_key"
        private const val SETTINGS_SHOW_FRAGMENT_ARGS = ":settings:show_fragment_args"
        private const val DEFAULT_BROWSER_APP_OPTION = "default_browser"
    }
}
