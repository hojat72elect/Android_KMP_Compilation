

package com.duckduckgo.app.notification

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.duckduckgo.app.notification.db.NotificationDao
import com.duckduckgo.app.notification.model.Notification
import com.duckduckgo.app.notification.model.SchedulableNotification
import com.duckduckgo.app.notification.model.SchedulableNotificationPlugin
import com.duckduckgo.common.utils.notification.checkPermissionAndNotify
import com.duckduckgo.common.utils.plugins.PluginPoint
import timber.log.Timber

class AppNotificationSender(
    private val context: Context,
    private val manager: NotificationManagerCompat,
    private val factory: NotificationFactory,
    private val notificationDao: NotificationDao,
    private val schedulableNotificationPluginPoint: PluginPoint<SchedulableNotificationPlugin>,
) : NotificationSender {

    override suspend fun sendNotification(notification: SchedulableNotification) {
        if (!notification.canShow() || notificationDao.exists(notification.id)) {
            Timber.v("Notification should not be shown")
            return
        }

        val specification = notification.buildSpecification()

        val notificationPlugin = schedulableNotificationPluginPoint.getPlugins().firstOrNull {
            notification.javaClass == it.getSchedulableNotification().javaClass
        }

        if (notificationPlugin == null) {
            Timber.v("No plugin found for notification class ${notification.javaClass}")
            return
        }

        val launchIntent = notificationPlugin.getLaunchIntent()
        val cancelIntent = NotificationHandlerService.pendingCancelNotificationHandlerIntent(context, notification.javaClass)
        val systemNotification = factory.createNotification(specification, launchIntent, cancelIntent)
        notificationDao.insert(Notification(notification.id))
        manager.checkPermissionAndNotify(context, specification.systemId, systemNotification)

        notificationPlugin.onNotificationShown()
    }
}
