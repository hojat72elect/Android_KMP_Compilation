

package com.duckduckgo.app.notification.model

import android.app.PendingIntent

interface SchedulableNotificationPlugin {
    fun getSchedulableNotification(): SchedulableNotification

    // TODO onNotificationLaunched is not used at the moment.
    //    fun onNotificationLaunched()
    fun onNotificationCancelled()
    fun onNotificationShown()
    fun getSpecification(): NotificationSpec
    fun getLaunchIntent(): PendingIntent?
}
