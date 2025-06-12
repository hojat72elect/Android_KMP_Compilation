package de.nulide.findmydevice.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.nulide.findmydevice.data.Settings
import de.nulide.findmydevice.data.SettingsRepository
import de.nulide.findmydevice.services.FmdBatteryLowService
import de.nulide.findmydevice.services.ServerConnectivityCheckService
import de.nulide.findmydevice.services.ServerLocationUploadService
import de.nulide.findmydevice.services.ServerVersionCheckService
import de.nulide.findmydevice.services.TempContactExpiredService
import de.nulide.findmydevice.utils.log


class BootReceiver : BroadcastReceiver() {

    companion object {
        private val TAG: String = BootReceiver::class.java.simpleName

        const val BOOT_COMPLETED: String = "android.intent.action.BOOT_COMPLETED"
    }

    override fun onReceive(context: Context?, intent: Intent) {
        val settings = SettingsRepository.Companion.getInstance(context!!)

        if (intent.action == BOOT_COMPLETED) {
            context.log().i(TAG, "Running BOOT_COMPLETED handler")

            doUpdateMigrations(context)

            TempContactExpiredService.scheduleJob(context, 0)

            if (settings.get(Settings.SET_FMD_LOW_BAT_SEND) as Boolean) {
                FmdBatteryLowService.scheduleJobNow(context)
            }

            if (settings.serverAccountExists()) {
                ServerLocationUploadService.scheduleJob(context, 0)
                ServerConnectivityCheckService.scheduleJob(context);
                ServerVersionCheckService.scheduleJobNow(context)
            }
        }
    }
}
