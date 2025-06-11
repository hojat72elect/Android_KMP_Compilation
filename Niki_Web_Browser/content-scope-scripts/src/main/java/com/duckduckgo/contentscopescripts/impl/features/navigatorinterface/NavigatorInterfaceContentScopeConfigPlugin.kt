package com.duckduckgo.contentscopescripts.impl.features.navigatorinterface

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.NavigatorInterfaceFeatureName.NavigatorInterface
import com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.store.NavigatorInterfaceRepository
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class NavigatorInterfaceContentScopeConfigPlugin @Inject constructor(
    private val navigatorInterfaceRepository: NavigatorInterfaceRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = NavigatorInterface.value
        val config = navigatorInterfaceRepository.navigatorInterfaceEntity.json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
