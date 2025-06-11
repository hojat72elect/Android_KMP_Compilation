

package com.duckduckgo.cookies.impl

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.cookies.api.CookiesFeatureName
import com.duckduckgo.cookies.store.contentscopescripts.ContentScopeScriptsCookieRepository
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class CookiesContentScopeConfigPlugin @Inject constructor(
    private val cookiesRepository: ContentScopeScriptsCookieRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = CookiesFeatureName.Cookie.value
        val config = cookiesRepository.getCookieEntity().json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
