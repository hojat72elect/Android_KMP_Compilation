

package com.duckduckgo.remote.messaging.impl.mappers

import com.duckduckgo.remote.messaging.api.RemoteMessage
import com.squareup.moshi.JsonAdapter
import dagger.Lazy

class MessageMapper(
    private val messageAdapter: Lazy<JsonAdapter<RemoteMessage>>,
) {

    fun toString(sitePayload: RemoteMessage): String {
        return messageAdapter.get().toJson(sitePayload)
    }

    fun fromMessage(payload: String): RemoteMessage? {
        return runCatching {
            messageAdapter.get().fromJson(payload)
        }.getOrNull()
    }
}
