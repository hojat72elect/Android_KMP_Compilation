

package com.duckduckgo.app.plugins

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.browser.api.JsInjectorPlugin
import com.duckduckgo.di.scopes.AppScope

@ContributesPluginPoint(
    scope = AppScope::class,
    boundType = JsInjectorPlugin::class,
)
@Suppress("unused")
interface UnusedJsInjectorPluginPoint
