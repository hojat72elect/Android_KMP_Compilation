package de.nulide.findmydevice.commands

import android.app.admin.DevicePolicyManager
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.nulide.findmydevice.R
import de.nulide.findmydevice.data.EncryptedSettingsRepository
import de.nulide.findmydevice.data.Settings
import de.nulide.findmydevice.permissions.DeviceAdminPermission
import de.nulide.findmydevice.services.FmdJobService
import de.nulide.findmydevice.transports.Transport
import de.nulide.findmydevice.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DeleteCommand(context: Context) : Command(context) {
    companion object {
        private val TAG = DeleteCommand::class.simpleName
    }

    override val keyword = "delete"
    override val usage = "delete <pin> [dryrun]"

    @get:DrawableRes
    override val icon = R.drawable.ic_delete_outline

    @get:StringRes
    override val shortDescription = R.string.cmd_delete_description_short

    override val longDescription = R.string.cmd_delete_description_long

    override val requiredPermissions = listOf(DeviceAdminPermission())

    override fun <T> executeInternal(
        args: List<String>,
        transport: Transport<T>,
        coroutineScope: CoroutineScope,
        job: FmdJobService?,
    ) {
        if (!(settings.get(Settings.SET_WIPE_ENABLED) as Boolean)) {
            val msg = context.getString(R.string.cmd_delete_response_disabled)
            context.log().i(TAG, msg)
            transport.send(context, msg)
            job?.jobFinished()
            return
        }

        if (args.isEmpty()) {
            val triggerWord = settings.get(Settings.SET_FMD_COMMAND) as String
            val fullUsage = "$triggerWord $usage"
            val msg = context.getString(R.string.cmd_delete_response_pwd_missing, fullUsage)
            context.log().i(TAG, msg)
            transport.send(context, msg)
            job?.jobFinished()
            return
        }
        val pwd = args[0]

        val encSettings = EncryptedSettingsRepository.getInstance(context)
        val expectedPin = encSettings.getFmdPin()
        if (expectedPin.isNullOrBlank() || expectedPin != pwd) {
            val msg = context.getString(R.string.cmd_delete_response_pwd_wrong)
            context.log().i(TAG, msg)
            transport.send(context, msg)
            job?.jobFinished()
            return
        }

        // Be defensive, match anything that might look right
        if (args.getOrNull(1)?.contains("dry") == true) {
            val msg = context.getString(R.string.cmd_delete_response_dry_run)
            context.log().i(TAG, msg)
            transport.send(context, msg)
            job?.jobFinished()
            return
        }

        coroutineScope.launch(Dispatchers.IO) {
            context.log().i(TAG, "Deleting device...")
            transport.send(context, context.getString(R.string.cmd_delete_response_success))

            // Give the message some time to be sent before the device is wiped
            delay(3000)

            val devicePolicyManager =
                context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

            // TODO: Use wipeDevice(), otherwise it won't work with targetSDK >= 34
            // See https://gitlab.com/Nulide/findmydevice/-/issues/199#note_1975457249
            // and https://gitlab.com/Nulide/findmydevice/-/issues/220
            try {
                devicePolicyManager.wipeData(0)
            } catch (e: Exception) {
                context.log().e(TAG, e.stackTraceToString())

                val msg = context.getString(R.string.cmd_delete_response_failed)
                context.log().i(TAG, msg)
                transport.send(context, msg)
            }

            job?.jobFinished()
        }
    }
}
