package com.amaze.fileutilities.utilis

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amaze.fileutilities.R

class NotificationConstants {
    companion object {

        private const val CHANNEL_NORMAL_ID = "normalChannel"
        const val TYPE_NORMAL = 0

        /**
         * This creates a channel (API >= 26) or applies the correct metadata to a notification (API < 26)
         */
        fun setMetadata(
            context: Context?,
            notification: NotificationCompat.Builder,
            type: Int
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                when (type) {
                    TYPE_NORMAL -> createNormalChannel(
                        context!!
                    )

                    else -> throw IllegalArgumentException("Unrecognized type:$type")
                }
            } else {
                when (type) {
                    TYPE_NORMAL -> {

                        notification.setCategory(Notification.CATEGORY_SERVICE)
                        notification.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        notification.priority = Notification.PRIORITY_LOW

                    }

                    else -> throw IllegalArgumentException("Unrecognized type:$type")
                }
            }
        }

        /**
         * You CANNOT call this from android < O. THis channel is set so it doesn't bother the user, with
         * the lowest importance.
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun createNormalChannel(context: Context) {
            val mNotificationManager = NotificationManagerCompat.from(context)
            if (mNotificationManager.getNotificationChannel(CHANNEL_NORMAL_ID) == null) {
                val mChannel = NotificationChannel(
                    CHANNEL_NORMAL_ID,
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW
                )
                // Configure the notification channel.
                mChannel.description = context.getString(R.string.app_name)
                mNotificationManager.createNotificationChannel(mChannel)
            }
        }
    }
}
