package de.nulide.findmydevice.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.nulide.findmydevice.data.Settings
import de.nulide.findmydevice.data.SettingsRepository
import de.nulide.findmydevice.services.ServerLocationUploadService
import de.nulide.findmydevice.utils.log


class BatteryLowReceiver : BroadcastReceiver() {

    companion object {
        val TAG = BatteryLowReceiver::class.simpleName

        private const val MIN_INTERVAL_MILLIS = 15 * 60 * 1000 // 15 mins

        fun handleLowBatteryUpload(context: Context) {
            val settings = SettingsRepository.getInstance(context)

            if (!(settings.get(Settings.SET_FMD_LOW_BAT_SEND) as Boolean)) {
                return
            }

            // Gson quirk: Gson may interpret long values as doubles.
            // This workaround ensures that the value is interpreted as a long.
            val lastUpload = (settings.get(Settings.SET_LAST_LOW_BAT_UPLOAD) as Number).toLong()
            val now = System.currentTimeMillis()

            // If the system fires the intent or notification too often, don't upload all the time.
            // https://stackoverflow.com/questions/47969335/intent-action-battery-low-broadcast-firing-every-ten-seconds-why
            if (lastUpload + MIN_INTERVAL_MILLIS < now) {
                context.log().i(TAG, "Low battery: uploading location.")
                settings.set(Settings.SET_LAST_LOW_BAT_UPLOAD, now)

                // Locating and uploading takes some time.
                // Start a service for this, to allow the BroadcastReceiver to exit.
                ServerLocationUploadService.scheduleJob(context, 0, false)
            } else {
                context.log().i(TAG, "Last low battery upload too recent, skipping.")
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Theoretically, this should work.
        // Practically, ACTION_BATTERY_LOW doesn't always seem to fire??
        // Therefore, keep the notification-based low-battery approach around for now.
        if (intent.action.equals(Intent.ACTION_BATTERY_LOW)) {
            context.log().i(TAG, "Received ACTION_BATTERY_LOW")
            handleLowBatteryUpload(context)
        }
    }
}
