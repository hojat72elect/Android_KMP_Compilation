

package com.duckduckgo.app.autofill

import android.content.Context

interface EmailProtectionJavascriptInjector {

    fun getAliasFunctions(
        context: Context,
        alias: String?,
    ): String

    fun getSignOutFunctions(
        context: Context,
    ): String
}
