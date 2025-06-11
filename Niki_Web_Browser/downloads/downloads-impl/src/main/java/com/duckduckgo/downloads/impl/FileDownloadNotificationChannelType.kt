

package com.duckduckgo.downloads.impl

import androidx.core.app.NotificationManagerCompat
import com.duckduckgo.app.notification.model.Channel

internal object FileDownloadNotificationChannelType {
    val FILE_DOWNLOADING = Channel(
        "com.duckduckgo.downloading",
        R.string.notificationChannelFileDownloading,
        NotificationManagerCompat.IMPORTANCE_LOW,
    )
    val FILE_DOWNLOADED = Channel(
        "com.duckduckgo.downloaded",
        R.string.notificationChannelFileDownloaded,
        NotificationManagerCompat.IMPORTANCE_LOW,
    )
}
