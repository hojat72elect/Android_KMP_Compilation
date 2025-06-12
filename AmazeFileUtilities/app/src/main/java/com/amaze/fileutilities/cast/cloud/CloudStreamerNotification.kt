package com.amaze.fileutilities.cast.cloud

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import com.amaze.fileutilities.R
import com.amaze.fileutilities.home_page.MainActivity
import com.amaze.fileutilities.utilis.NotificationConstants
import com.amaze.fileutilities.utilis.Utils.Companion.getPendingIntentFlag

class CloudStreamerNotification {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "httpServerChannel"
        const val NOTIFICATION_ID = 1006

        fun startNotification(context: Context): Notification {
            val builder = buildNotification(
                context,
                R.string.cast_notification_title,
                context.getString(R.string.cast_notification_summary)
            )
            return builder.build()
        }

        private fun buildNotification(
            context: Context,
            @StringRes contentTitleRes: Int,
            contentText: String
        ): NotificationCompat.Builder {
            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val contentIntent = PendingIntent.getActivity(
                context, 0,
                notificationIntent, getPendingIntentFlag(0)
            )
            val `when` = System.currentTimeMillis()
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(context.getString(contentTitleRes))
                    .setContentText(contentText)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.ic_baseline_cast_32)
                    .setTicker(context.getString(R.string.cast_notification_title))
                    .setWhen(`when`)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
            val stopIcon = android.R.drawable.ic_menu_close_clear_cancel
            val stopText: CharSequence = context.getString(R.string.stop)
            val stopIntent: Intent =
                Intent(CloudStreamerService.TAG_BROADCAST_STREAMER_STOP)
                    .setPackage(context.packageName)
            val stopPendingIntent =
                PendingIntent.getBroadcast(
                    context, 0, stopIntent,
                    getPendingIntentFlag(PendingIntent.FLAG_ONE_SHOT)
                )

            builder.addAction(stopIcon, stopText, stopPendingIntent)
            NotificationConstants.setMetadata(context, builder, NotificationConstants.TYPE_NORMAL)
            return builder
        }
    }
}
