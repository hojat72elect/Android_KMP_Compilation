package de.nulide.findmydevice.commands

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.nulide.findmydevice.R
import de.nulide.findmydevice.data.Settings
import de.nulide.findmydevice.permissions.CameraPermission
import de.nulide.findmydevice.services.FmdJobService
import de.nulide.findmydevice.transports.Transport
import de.nulide.findmydevice.ui.DummyCameraxActivity
import de.nulide.findmydevice.utils.log
import kotlinx.coroutines.CoroutineScope


class CameraCommand(context: Context) : Command(context) {
    companion object {
        private val TAG = CameraCommand::class.simpleName
    }

    override val keyword = "camera"
    override val usage = "camera [front | back]"

    @get:DrawableRes
    override val icon = R.drawable.ic_camera

    @get:StringRes
    override val shortDescription = R.string.cmd_camera_description_short

    override val longDescription = R.string.cmd_camera_description_long

    override val requiredPermissions = listOf(CameraPermission())

    override fun <T> executeInternal(
        args: List<String>,
        transport: Transport<T>,
        coroutineScope: CoroutineScope,
        job: FmdJobService?,
    ) {
        if (!settings.serverAccountExists()) {
            context.log().w(TAG, "Cannot take picture: no FMD Server account")
            transport.send(context, context.getString(R.string.cmd_camera_response_no_fmd_server))
            job?.jobFinished()
            return
        }

        val dummyCameraActivity = Intent(context, DummyCameraxActivity::class.java)
        dummyCameraActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (args.contains("front")) {
            dummyCameraActivity.putExtra(
                DummyCameraxActivity.EXTRA_CAMERA,
                DummyCameraxActivity.CAMERA_FRONT
            )
        } else {
            dummyCameraActivity.putExtra(
                DummyCameraxActivity.EXTRA_CAMERA,
                DummyCameraxActivity.CAMERA_BACK
            )
        }
        context.log().d(TAG, "Starting camera activity")
        context.startActivity(dummyCameraActivity)

        val serverUrl = settings.get(Settings.SET_FMDSERVER_URL) as String
        transport.send(context, context.getString(R.string.cmd_camera_response_success, serverUrl))
        job?.jobFinished()
    }
}
