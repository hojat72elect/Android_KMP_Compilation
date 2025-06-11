

package com.duckduckgo.common.utils.notification

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat

fun NotificationManagerCompat.checkPermissionAndNotify(
    context: Context,
    id: Int,
    notification: Notification,
) {
    runCatching {
        if (ActivityCompat.checkSelfPermission(context, POST_NOTIFICATIONS) == PERMISSION_GRANTED) {
            notify(id, notification)
        }
    }
}
