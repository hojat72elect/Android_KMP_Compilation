

package com.duckduckgo.remote.messaging.impl.mappers

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.remote.messaging.api.JsonToMatchingAttributeMapper
import com.duckduckgo.remote.messaging.api.MessageActionMapperPlugin
import com.duckduckgo.remote.messaging.impl.models.JsonRemoteMessagingConfig
import com.duckduckgo.remote.messaging.impl.models.RemoteConfig
import timber.log.Timber

class RemoteMessagingConfigJsonMapper(
    private val appBuildConfig: AppBuildConfig,
    private val matchingAttributeMappers: Set<JsonToMatchingAttributeMapper>,
    private val actionMappers: Set<MessageActionMapperPlugin>,
) {
    fun map(jsonRemoteMessagingConfig: JsonRemoteMessagingConfig): RemoteConfig {
        val messages = jsonRemoteMessagingConfig.messages.mapToRemoteMessage(appBuildConfig.deviceLocale, actionMappers)
        Timber.i("RMF: messages parsed $messages")
        val rules = jsonRemoteMessagingConfig.rules.mapToMatchingRules(matchingAttributeMappers)
        return RemoteConfig(
            messages = messages,
            rules = rules,
        )
    }
}
