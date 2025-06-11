

package com.duckduckgo.mobile.android.vpn.ui.notification

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import com.duckduckgo.mobile.android.vpn.pixels.DeviceShieldPixels
import javax.inject.Inject
import logcat.LogPriority
import logcat.logcat

private const val INVALID_NOTIFICATION_VARIANT = -1

class ReminderNotificationPressedHandler @Inject constructor(
    private val deviceShieldPixels: DeviceShieldPixels,
) : ResultReceiver(Handler(Looper.getMainLooper())) {
    override fun onReceiveResult(
        resultCode: Int,
        resultData: Bundle?,
    ) {
        logcat { "Reminder notification pressed" }
        deviceShieldPixels.didPressReminderNotification()
    }
}

class OngoingNotificationPressedHandler @Inject constructor(
    private val deviceShieldPixels: DeviceShieldPixels,
) : ResultReceiver(Handler(Looper.getMainLooper())) {
    override fun onReceiveResult(
        resultCode: Int,
        resultData: Bundle?,
    ) {
        logcat { "Ongoing notification pressed" }
        deviceShieldPixels.didPressOngoingNotification()
    }
}

class WeeklyNotificationPressedHandler @Inject constructor(
    private val deviceShieldPixels: DeviceShieldPixels,
) : ResultReceiver(Handler(Looper.getMainLooper())) {

    var notificationVariant: Int = INVALID_NOTIFICATION_VARIANT

    override fun onReceiveResult(
        resultCode: Int,
        resultData: Bundle?,
    ) {
        logcat { "Weekly notification pressed" }
        if (notificationVariant == INVALID_NOTIFICATION_VARIANT) {
            logcat(LogPriority.ERROR) { "Weekly notification pressed reported with uninitialized notification variant" }
        } else {
            deviceShieldPixels.didPressOnWeeklyNotification(notificationVariant)
        }
    }
}

class DailyNotificationPressedHandler @Inject constructor(
    private val deviceShieldPixels: DeviceShieldPixels,
) : ResultReceiver(Handler(Looper.getMainLooper())) {

    var notificationVariant: Int = INVALID_NOTIFICATION_VARIANT

    override fun onReceiveResult(
        resultCode: Int,
        resultData: Bundle?,
    ) {
        logcat { "Daily notification pressed" }
        if (notificationVariant == INVALID_NOTIFICATION_VARIANT) {
            logcat(LogPriority.ERROR) { "Daily notification pressed reported with uninitialized notification variant" }
        } else {
            deviceShieldPixels.didPressOnDailyNotification(notificationVariant)
        }
    }
}
