

package com.duckduckgo.app.global.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.defaultbrowsing.DefaultBrowserSystemSettings
import timber.log.Timber

fun FragmentActivity.launchExternalActivity(intent: Intent) {
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        Toast.makeText(this, R.string.no_compatible_third_party_app_installed, Toast.LENGTH_SHORT).show()
    }
}

fun Context.launchDefaultAppActivity() {
    try {
        val intent = DefaultBrowserSystemSettings.intent()
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val errorMessage = getString(R.string.cannotLaunchDefaultAppSettings)
        Timber.w(errorMessage)
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
