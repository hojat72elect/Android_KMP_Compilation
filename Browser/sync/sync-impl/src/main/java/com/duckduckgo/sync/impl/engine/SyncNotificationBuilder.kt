

package com.duckduckgo.sync.impl.engine

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.sync.api.SYNC_NOTIFICATION_CHANNEL_ID
import com.duckduckgo.sync.api.SyncActivityWithEmptyParams
import com.duckduckgo.sync.impl.R
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface SyncNotificationBuilder {
    fun buildSyncPausedNotification(context: Context, addNavigationIntent: Boolean = true): Notification
    fun buildSyncErrorNotification(context: Context): Notification
    fun buildSyncSignedOutNotification(context: Context): Notification
}

@ContributesBinding(AppScope::class)
class AppCredentialsSyncNotificationBuilder @Inject constructor(
    private val globalGlobalActivityStarter: GlobalActivityStarter,
) : SyncNotificationBuilder {
    override fun buildSyncPausedNotification(context: Context, addNavigationIntent: Boolean): Notification {
        val notificationBuilder = NotificationCompat.Builder(context, SYNC_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(com.duckduckgo.mobile.android.R.drawable.notification_logo)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(RemoteViews(context.packageName, R.layout.notification_sync_paused))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
        if (addNavigationIntent) {
            notificationBuilder.setContentIntent(getPendingIntent(context))
        }
        return notificationBuilder.build()
    }

    override fun buildSyncErrorNotification(
        context: Context,
    ): Notification {
        val notificationBuilder = NotificationCompat.Builder(context, SYNC_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(com.duckduckgo.mobile.android.R.drawable.notification_logo)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(RemoteViews(context.packageName, R.layout.notification_sync_error))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
        return notificationBuilder.build()
    }

    override fun buildSyncSignedOutNotification(
        context: Context,
    ): Notification {
        val notificationBuilder = NotificationCompat.Builder(context, SYNC_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(com.duckduckgo.mobile.android.R.drawable.notification_logo)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(RemoteViews(context.packageName, R.layout.notification_sync_signed_out))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
        notificationBuilder.setContentIntent(getPendingIntent(context))
        return notificationBuilder.build()
    }

    private fun getPendingIntent(context: Context): PendingIntent? = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(
            globalGlobalActivityStarter.startIntent(context, SyncActivityWithEmptyParams)!!,
        )
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}
