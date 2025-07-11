

package com.duckduckgo.app.global

import android.content.Context
import android.content.Intent

interface DefaultRoleBrowserDialog {
    fun createIntent(context: Context): Intent?
    fun shouldShowDialog(): Boolean
    fun dialogShown()
}
