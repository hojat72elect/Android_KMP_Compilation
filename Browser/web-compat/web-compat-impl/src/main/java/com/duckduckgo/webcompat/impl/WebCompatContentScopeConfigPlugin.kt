

package com.duckduckgo.webcompat.impl

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.webcompat.store.WebCompatRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class WebCompatContentScopeConfigPlugin @Inject constructor(
    private val webCompatRepository: WebCompatRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = WebCompatFeatureName.WebCompat.value
        val config = webCompatRepository.getWebCompatEntity().json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
