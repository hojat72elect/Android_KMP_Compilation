

package com.duckduckgo.app.autofill

import android.content.Context
import com.duckduckgo.app.browser.R
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class DefaultEmailProtectionJavascriptInjector @Inject constructor() : EmailProtectionJavascriptInjector {
    private lateinit var aliasFunctions: String
    private lateinit var signOutFunctions: String

    override fun getAliasFunctions(
        context: Context,
        alias: String?,
    ): String {
        if (!this::aliasFunctions.isInitialized) {
            aliasFunctions = context.resources.openRawResource(R.raw.inject_alias).bufferedReader().use { it.readText() }
        }
        return aliasFunctions.replace("%s", alias.orEmpty())
    }

    override fun getSignOutFunctions(
        context: Context,
    ): String {
        if (!this::signOutFunctions.isInitialized) {
            signOutFunctions = context.resources.openRawResource(R.raw.signout_autofill).bufferedReader().use { it.readText() }
        }
        return signOutFunctions
    }
}
