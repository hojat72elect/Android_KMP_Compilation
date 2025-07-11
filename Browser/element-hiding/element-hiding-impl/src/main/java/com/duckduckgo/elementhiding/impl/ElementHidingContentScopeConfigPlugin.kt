

package com.duckduckgo.elementhiding.impl

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.elementhiding.store.ElementHidingRepository
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class ElementHidingContentScopeConfigPlugin @Inject constructor(
    private val elementHidingRepository: ElementHidingRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = ElementHidingFeatureName.ElementHiding.value
        val config = elementHidingRepository.elementHidingEntity.json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
