

package com.duckduckgo.remote.messaging.api

import kotlinx.coroutines.flow.Flow

interface RemoteMessagingRepository {
    fun getMessageById(id: String): RemoteMessage?
    fun activeMessage(message: RemoteMessage?)
    fun message(): RemoteMessage?
    fun messageFlow(): Flow<RemoteMessage?>
    suspend fun dismissMessage(id: String)
    fun dismissedMessages(): List<String>
    fun didShow(id: String): Boolean
    fun markAsShown(remoteMessage: RemoteMessage)
}
