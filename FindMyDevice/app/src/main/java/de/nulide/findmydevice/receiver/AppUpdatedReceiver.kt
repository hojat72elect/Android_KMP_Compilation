package de.nulide.findmydevice.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.nulide.findmydevice.data.SettingsRepository
import de.nulide.findmydevice.services.ServerConnectivityCheckService
import de.nulide.findmydevice.services.ServerLocationUploadService
import de.nulide.findmydevice.services.ServerVersionCheckService
import de.nulide.findmydevice.services.TempContactExpiredService
import de.nulide.findmydevice.ui.onboarding.PinUpdate
import de.nulide.findmydevice.ui.onboarding.UpdateboardingModernCryptoActivity
import de.nulide.findmydevice.utils.log


class AppUpdatedReceiver : BroadcastReceiver() {

    companion object {
        private val TAG: String = AppUpdatedReceiver::class.java.simpleName

        const val APP_UPDATED: String = "android.intent.action.MY_PACKAGE_REPLACED"
    }

    override fun onReceive(context: Context?, intent: Intent) {
        val settings = SettingsRepository.getInstance(context!!)

        if (intent.action == APP_UPDATED) {
            context.log().i(TAG, "Running MY_PACKAGE_REPLACED (APP_UPDATED) handler")

            doUpdateMigrations(context)

            TempContactExpiredService.scheduleJob(context, 0)

            if (settings.serverAccountExists()) {
                ServerLocationUploadService.scheduleJob(context, 0)
                ServerConnectivityCheckService.scheduleJob(context)
                ServerVersionCheckService.scheduleJobNow(context)
            }
        }
    }
}

fun doUpdateMigrations(context: Context) {
    val settings = SettingsRepository.getInstance(context)
    settings.migrateSettings()
    UpdateboardingModernCryptoActivity.notifyAboutCryptoRefreshIfRequired(context)
    PinUpdate.migratePin(context)
}
