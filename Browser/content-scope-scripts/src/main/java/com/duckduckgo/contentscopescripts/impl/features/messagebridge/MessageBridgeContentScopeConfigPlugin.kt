package com.duckduckgo.contentscopescripts.impl.features.messagebridge

import com.duckduckgo.contentscopescripts.api.ContentScopeConfigPlugin
import com.duckduckgo.contentscopescripts.impl.features.messagebridge.MessageBridgeFeatureName.MessageBridge
import com.duckduckgo.contentscopescripts.impl.features.messagebridge.store.MessageBridgeRepository
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class MessageBridgeContentScopeConfigPlugin @Inject constructor(
    private val messageBridgeRepository: MessageBridgeRepository,
) : ContentScopeConfigPlugin {

    override fun config(): String {
        val featureName = MessageBridge.value
        val config = messageBridgeRepository.messageBridgeEntity.json
        return "\"$featureName\":$config"
    }

    override fun preferences(): String? {
        return null
    }
}
