package de.nulide.findmydevice.commands

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.nulide.findmydevice.R
import de.nulide.findmydevice.permissions.LocationPermission
import de.nulide.findmydevice.services.FmdJobService
import de.nulide.findmydevice.transports.Transport
import de.nulide.findmydevice.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope


class StatsCommand(context: Context) : Command(context) {

    override val keyword = "stats"
    override val usage = "stats"

    @get:DrawableRes
    override val icon = R.drawable.ic_cell_wifi

    @get:StringRes
    override val shortDescription = R.string.cmd_stats_description_short

    override val longDescription = R.string.cmd_stats_description_long

    override val requiredPermissions = listOf(LocationPermission())

    override fun <T> executeInternal(
        args: List<String>,
        transport: Transport<T>,
        coroutineScope: CoroutineScope,
        job: FmdJobService?,
    ) {
        val ips = NetworkUtils.getAllIP()
        val ipsString = ips.map { i -> i.value }.joinToString("\n")

        val wifis = NetworkUtils.getWifiNetworks(context)
        val wifisString = wifis
            .map { sr -> "SSID: ${sr.SSID}\nBSSID: ${sr.BSSID}" }
            .joinToString("\n\n")

        val reply = context.getString(R.string.cmd_stats_response, ipsString, wifisString)

        transport.send(context, reply)
        job?.jobFinished()
    }
}
