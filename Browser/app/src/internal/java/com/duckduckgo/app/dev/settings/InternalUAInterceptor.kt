

package com.duckduckgo.app.dev.settings

import com.duckduckgo.app.dev.settings.db.DevSettingsDataStore
import com.duckduckgo.app.dev.settings.db.UAOverride
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.user.agent.api.UserAgentInterceptor
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

@ContributesMultibinding(AppScope::class)
class InternalUAInterceptor @Inject constructor(
    private val devSettingsDataStore: DevSettingsDataStore,
    @Named("defaultUserAgent") private val defaultUserAgent: Provider<String>,
) : UserAgentInterceptor {

    override fun intercept(userAgent: String): String {
        if (!devSettingsDataStore.overrideUA) return userAgent

        return when (devSettingsDataStore.selectedUA) {
            UAOverride.DEFAULT -> userAgent
            UAOverride.FIREFOX -> "Mozilla/5.0 (Android 11; Mobile; rv:94.0) Gecko/94.0 Firefox/94.0"
            UAOverride.WEBVIEW -> defaultUserAgent.get()
        }
    }
}
