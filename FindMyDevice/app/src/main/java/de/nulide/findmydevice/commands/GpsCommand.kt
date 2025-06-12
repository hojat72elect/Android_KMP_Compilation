package de.nulide.findmydevice.commands

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.nulide.findmydevice.R
import de.nulide.findmydevice.permissions.WriteSecureSettingsPermission
import de.nulide.findmydevice.services.FmdJobService
import de.nulide.findmydevice.transports.Transport
import de.nulide.findmydevice.utils.SecureSettings
import kotlinx.coroutines.CoroutineScope


class GpsCommand(context: Context) : Command(context) {

    override val keyword = "gps"
    override val usage = "gps [on | off]"

    @get:DrawableRes
    override val icon = R.drawable.ic_satellite

    @get:StringRes
    override val shortDescription = R.string.cmd_gps_description_short

    override val longDescription = null

    override val requiredPermissions = listOf(WriteSecureSettingsPermission())

    override fun <T> executeInternal(
        args: List<String>,
        transport: Transport<T>,
        coroutineScope: CoroutineScope,
        job: FmdJobService?,
    ) {
        if (args.contains("on")) {
            SecureSettings.turnGPS(context, true)
            transport.send(context, context.getString(R.string.cmd_gps_response_on))
        } else if (args.contains("off")) {
            SecureSettings.turnGPS(context, false)
            transport.send(context, context.getString(R.string.cmd_gps_response_off))
        }
        job?.jobFinished()
    }
}
