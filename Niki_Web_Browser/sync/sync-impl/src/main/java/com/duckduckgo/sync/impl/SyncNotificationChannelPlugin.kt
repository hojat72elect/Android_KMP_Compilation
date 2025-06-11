

package com.duckduckgo.sync.impl

import androidx.core.app.NotificationManagerCompat
import com.duckduckgo.app.notification.model.Channel
import com.duckduckgo.app.notification.model.NotificationPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.api.SYNC_NOTIFICATION_CHANNEL_ID
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class SyncNotificationChannelPlugin @Inject constructor() : NotificationPlugin {
    override fun getChannels(): List<Channel> {
        return listOf(
            SyncNotificationChannelType.SYNC_STATE,
        )
    }
}

internal object SyncNotificationChannelType {
    val SYNC_STATE = Channel(
        SYNC_NOTIFICATION_CHANNEL_ID,
        R.string.sync_notification_channel_name,
        NotificationManagerCompat.IMPORTANCE_HIGH,
    )
}
