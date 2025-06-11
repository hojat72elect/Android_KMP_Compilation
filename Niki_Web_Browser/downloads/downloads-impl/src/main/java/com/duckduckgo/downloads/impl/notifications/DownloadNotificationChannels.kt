

package com.duckduckgo.downloads.impl.notifications

import com.duckduckgo.app.notification.model.Channel
import com.duckduckgo.app.notification.model.NotificationPlugin
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.downloads.impl.FileDownloadNotificationChannelType
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
class DownloadNotificationChannels @Inject constructor() : NotificationPlugin {
    override fun getChannels(): List<Channel> {
        return listOf(
            FileDownloadNotificationChannelType.FILE_DOWNLOADING,
            FileDownloadNotificationChannelType.FILE_DOWNLOADED,
        )
    }
}
