

package com.duckduckgo.app.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.duckduckgo.app.notification.model.NotificationSpec
import javax.inject.Inject

class NotificationFactory @Inject constructor(
    val context: Context,
    val manager: NotificationManagerCompat,
) {

    fun createNotification(
        specification: NotificationSpec,
        launchIntent: PendingIntent?,
        cancelIntent: PendingIntent?,
    ): Notification {
        val builder = NotificationCompat.Builder(context, specification.channel.id)
            .setPriority(specification.channel.priority)
            .setSmallIcon(specification.icon)
            .setContentTitle(specification.title)
            .setContentText(specification.description)
            .setStyle(NotificationCompat.BigTextStyle().bigText(specification.description))
            .setColor(specification.color)
            .setContentIntent(launchIntent)
            .setDeleteIntent(cancelIntent)
            .setAutoCancel(specification.autoCancel)

        specification.launchButton?.let {
            builder.addAction(specification.icon, it, launchIntent)
        }

        specification.closeButton?.let {
            builder.addAction(specification.icon, it, cancelIntent)
        }

        return builder.build()
    }
}
