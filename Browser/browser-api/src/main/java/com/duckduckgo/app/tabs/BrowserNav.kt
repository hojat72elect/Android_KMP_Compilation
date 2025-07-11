

package com.duckduckgo.app.tabs

import android.content.Context
import android.content.Intent

/**
 * Public interface to provide navigation Intents related to browser screen
 */
interface BrowserNav {
    // opens url on a new tab
    fun openInNewTab(context: Context, url: String): Intent
    fun openInCurrentTab(context: Context, url: String): Intent
}
