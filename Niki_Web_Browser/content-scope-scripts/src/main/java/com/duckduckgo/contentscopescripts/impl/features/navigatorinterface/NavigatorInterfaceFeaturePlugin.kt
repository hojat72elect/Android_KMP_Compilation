package com.duckduckgo.contentscopescripts.impl.features.navigatorinterface

import com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.NavigatorInterfaceFeatureName.NavigatorInterface
import com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.store.NavigatorInterfaceEntity
import com.duckduckgo.contentscopescripts.impl.features.navigatorinterface.store.NavigatorInterfaceRepository
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class NavigatorInterfaceFeaturePlugin @Inject constructor(
    private val navigatorInterfaceRepository: NavigatorInterfaceRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        val navigatorInterfaceFeatureName = navigatorInterfaceFeatureValueOf(featureName) ?: return false
        if (navigatorInterfaceFeatureName.value == this.featureName) {
            val entity = NavigatorInterfaceEntity(json = jsonString)
            navigatorInterfaceRepository.updateAll(navigatorInterfaceEntity = entity)
            return true
        }
        return false
    }

    override val featureName: String = NavigatorInterface.value
}
