

package com.duckduckgo.app.notification

import com.duckduckgo.app.notification.model.SchedulableNotification

interface NotificationSender {
    suspend fun sendNotification(notification: SchedulableNotification)
}
