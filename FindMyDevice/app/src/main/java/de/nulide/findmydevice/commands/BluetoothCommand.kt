package de.nulide.findmydevice.commands

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.nulide.findmydevice.R
import de.nulide.findmydevice.permissions.BluetoothConnectPermission
import de.nulide.findmydevice.permissions.Permission
import de.nulide.findmydevice.services.FmdJobService
import de.nulide.findmydevice.transports.Transport
import kotlinx.coroutines.CoroutineScope


class BluetoothCommand(context: Context) : Command(context) {

    override val keyword = "bluetooth"
    override val usage = "bluetooth [on | off]"

    @get:DrawableRes
    override val icon = R.drawable.ic_bluetooth

    @get:StringRes
    override val shortDescription = R.string.cmd_bluetooth_description_short

    override val longDescription = null

    override val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(BluetoothConnectPermission())
        // TODO: device owner
    } else {
        emptyList<Permission>()
    }

    @SuppressLint("MissingPermission")
    override fun <T> executeInternal(
        args: List<String>,
        transport: Transport<T>,
        coroutineScope: CoroutineScope,
        job: FmdJobService?,
    ) {
        val bluetoothManager: BluetoothManager =
            context.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            transport.send(context, context.getString(R.string.cmd_bluetooth_response_no_bluetooth))
            return
        }

        if (args.contains("on")) {
            bluetoothAdapter.enable()
            transport.send(context, context.getString(R.string.cmd_bluetooth_response_on))
        } else if (args.contains("off")) {
            bluetoothAdapter.disable()
            transport.send(context, context.getString(R.string.cmd_bluetooth_response_off))
        }
        job?.jobFinished()
    }
}
