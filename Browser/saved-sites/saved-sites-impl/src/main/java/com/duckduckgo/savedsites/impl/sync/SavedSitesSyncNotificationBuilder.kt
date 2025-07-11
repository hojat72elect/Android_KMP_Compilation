

package com.duckduckgo.savedsites.impl.sync

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.navigation.api.GlobalActivityStarter
import com.duckduckgo.saved.sites.impl.R.layout
import com.duckduckgo.sync.api.SYNC_NOTIFICATION_CHANNEL_ID
import com.duckduckgo.sync.api.SyncActivityWithEmptyParams
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface SavedSitesSyncNotificationBuilder {
    fun buildRateLimitNotification(context: Context): Notification
    fun buildInvalidRequestNotification(context: Context): Notification
}

@ContributesBinding(AppScope::class)
class AppSavedSitesSyncNotificationBuilder @Inject constructor(
    private val globalGlobalActivityStarter: GlobalActivityStarter,
) : SavedSitesSyncNotificationBuilder {
    override fun buildRateLimitNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, SYNC_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(com.duckduckgo.mobile.android.R.drawable.notification_logo)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(getPendingIntent(context))
            .setCustomContentView(RemoteViews(context.packageName, layout.notification_rate_limit))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .build()
    }

    override fun buildInvalidRequestNotification(context: Context): Notification {
        return NotificationCompat.Builder(context, SYNC_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(com.duckduckgo.mobile.android.R.drawable.notification_logo)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(getPendingIntent(context))
            .setCustomContentView(RemoteViews(context.packageName, layout.notification_invalid_request))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .build()
    }

    private fun getPendingIntent(context: Context): PendingIntent? = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(
            globalGlobalActivityStarter.startIntent(context, SyncActivityWithEmptyParams)!!,
        )
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}
