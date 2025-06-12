package de.nulide.findmydevice

import android.app.Application
import de.nulide.findmydevice.data.SettingsRepository
import de.nulide.findmydevice.data.UncaughtExceptionHandler.Companion.initUncaughtExceptionHandler
import de.nulide.findmydevice.receiver.PushReceiver
import de.nulide.findmydevice.services.ServerConnectivityCheckService
import de.nulide.findmydevice.services.ServerLocationUploadService
import de.nulide.findmydevice.services.TempContactExpiredService
import de.nulide.findmydevice.utils.Notifications


class FmdApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Notifications.init(this)
        initUncaughtExceptionHandler(this)

        val settings = SettingsRepository.getInstance(this)
        if (settings.serverAccountExists()) {
            PushReceiver.registerWithUnifiedPush(this)
            ServerLocationUploadService.scheduleJob(this, 0)
            ServerConnectivityCheckService.scheduleJob(this)
        } else {
            // just in case they were still running
            ServerLocationUploadService.cancelJob(this)
            ServerConnectivityCheckService.cancelJob(this)
            PushReceiver.unregisterWithUnifiedPush(this)
        }

        TempContactExpiredService.scheduleJob(this, 0)
    }
}
