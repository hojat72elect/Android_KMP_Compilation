package com.duckduckgo.contentscopescripts.impl.features.messagebridge

import com.duckduckgo.contentscopescripts.impl.features.messagebridge.MessageBridgeFeatureName.MessageBridge
import com.duckduckgo.contentscopescripts.impl.features.messagebridge.store.MessageBridgeEntity
import com.duckduckgo.contentscopescripts.impl.features.messagebridge.store.MessageBridgeRepository
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyFeaturePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class MessageBridgeFeaturePlugin @Inject constructor(
    private val messageBridgeRepository: MessageBridgeRepository,
) : PrivacyFeaturePlugin {

    override fun store(featureName: String, jsonString: String): Boolean {
        val messageBridgeFeatureName = messageBridgeFeatureValueOf(featureName) ?: return false
        if (messageBridgeFeatureName.value == this.featureName) {
            val entity = MessageBridgeEntity(json = jsonString)
            messageBridgeRepository.updateAll(messageBridgeEntity = entity)
            return true
        }
        return false
    }

    override val featureName: String = MessageBridge.value
}
