package de.nulide.findmydevice.services

import android.provider.Telephony
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import de.nulide.findmydevice.commands.CommandHandler
import de.nulide.findmydevice.data.Settings
import de.nulide.findmydevice.data.SettingsRepository
import de.nulide.findmydevice.receiver.BatteryLowReceiver
import de.nulide.findmydevice.transports.NotificationReplyTransport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class NotificationListenService : NotificationListenerService() {

    companion object {
        // LineageOS 21 / Android 14: android, BatterySaverStateMachine
        private val BATTERY_PACKAGE_NAMES = listOf("com.android.systemui", "android")
        private val BATTERY_TAGS = listOf("low_battery", "BatterySaverStateMachine")
    }

    private lateinit var settings: SettingsRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        settings = SettingsRepository.getInstance(this)

        // SMS is handled separately
        val packageName = sbn.packageName
        if (packageName == Telephony.Sms.getDefaultSmsPackage(this)) {
            return
        }

        if (settings.get(Settings.SET_FMD_LOW_BAT_SEND) as Boolean) {
            if (packageName in BATTERY_PACKAGE_NAMES) {
                val tag = sbn.tag
                if (tag != null && tag in BATTERY_TAGS) {
                    BatteryLowReceiver.handleLowBatteryUpload(this)
                    return
                }
            }
        }

        val messageChars = sbn.notification.extras.getCharSequence("android.text") ?: return
        val message = messageChars.toString()

        // Early sanity check + abort
        val fmdTriggerWord = (settings.get(Settings.SET_FMD_COMMAND) as String)
        if (!message.startsWith(fmdTriggerWord, ignoreCase = true)) {
            return
        }

        val transport = NotificationReplyTransport(sbn)
        val commandHandler = CommandHandler(transport, coroutineScope, null)
        commandHandler.execute(this, message) { cancelNotification(sbn.key) }
    }
}
