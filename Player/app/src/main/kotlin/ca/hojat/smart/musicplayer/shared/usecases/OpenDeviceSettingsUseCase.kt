package ca.hojat.smart.musicplayer.shared.usecases

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object OpenDeviceSettingsUseCase {

    /**
     * Programmatically direct the user to the application's settings page within the device's settings.
     */
    operator fun invoke(context: Context){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            ShowToastUseCase(context, "The error : $e")
        }
    }
}