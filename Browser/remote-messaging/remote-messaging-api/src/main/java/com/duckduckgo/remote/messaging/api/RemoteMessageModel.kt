

package com.duckduckgo.remote.messaging.api

import kotlinx.coroutines.flow.Flow

interface RemoteMessageModel {

    fun getActiveMessage(): RemoteMessage?

    fun getActiveMessages(): Flow<RemoteMessage?>

    suspend fun onMessageShown(remoteMessage: RemoteMessage)

    suspend fun onMessageDismissed(remoteMessage: RemoteMessage)

    suspend fun onPrimaryActionClicked(remoteMessage: RemoteMessage): Action?

    suspend fun onSecondaryActionClicked(remoteMessage: RemoteMessage): Action?

    suspend fun onActionClicked(remoteMessage: RemoteMessage): Action?
}
