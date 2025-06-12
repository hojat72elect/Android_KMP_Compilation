package de.nulide.findmydevice.commands

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.nulide.findmydevice.R
import de.nulide.findmydevice.locationproviders.CellLocationProvider
import de.nulide.findmydevice.locationproviders.GpsLocationProvider
import de.nulide.findmydevice.locationproviders.LocationAutoOnOffHandler
import de.nulide.findmydevice.permissions.LocationPermission
import de.nulide.findmydevice.permissions.WriteSecureSettingsPermission
import de.nulide.findmydevice.services.FmdJobService
import de.nulide.findmydevice.transports.Transport
import de.nulide.findmydevice.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch


class LocateCommand(context: Context) : Command(context) {

    companion object {
        private val TAG = LocateCommand::class.simpleName
    }

    override val keyword = "locate"
    override val usage = "locate [last | all | cell | gps]"

    @get:DrawableRes
    override val icon = R.drawable.ic_location

    @get:StringRes
    override val shortDescription = R.string.cmd_locate_description_short

    @get:StringRes
    override val longDescription = R.string.cmd_locate_description_long

    override val requiredPermissions = listOf(LocationPermission())

    override val optionalPermissions = listOf(WriteSecureSettingsPermission())

    override fun <T> executeInternal(
        args: List<String>,
        transport: Transport<T>,
        coroutineScope: CoroutineScope,
        job: FmdJobService?,
    ) {
        // ignore everything except the first option (if it exists)
        val option = args.getOrElse(0) { "all" }

        // fmd locate last
        if (args.contains("last")) {
            coroutineScope.launch(Dispatchers.IO) {
                GpsLocationProvider(context, transport).getLastKnownLocation()
                job?.jobFinished()
            }
            // Even if last location is not available, return here.
            // Because requesting "last" explicitly asks not to refresh the location.
            return
        }

        val locOnOffHandler = LocationAutoOnOffHandler.getInstance(context)
        val res = locOnOffHandler.addJob()

        if (!res.isLocationOn) {
            context.log().w(
                TAG,
                "Cannot locate: Location is off and missing permission WRITE_SECURE_SETTINGS"
            )
            transport.send(context, context.getString(R.string.cmd_locate_response_location_off))
            return
        }

        // build the location providers
        val providers = when (option) {
            "cell" -> listOf(CellLocationProvider(context, transport))
            "gps" -> listOf(GpsLocationProvider(context, transport))
            else ->
                listOf(
                    GpsLocationProvider(context, transport),
                    CellLocationProvider(context, transport)
                )
        }

        val cleanupHandler = {
            locOnOffHandler.removeJob(res.jobId)
            job?.jobFinished()
        }

        // Make sure we clean up and properly finish the job, even when something else
        // cancels the coroutine. E.g., when the system calls onStopJob() the coroutine
        // is cancelled.
        //
        // Proper cleanup is important for the regular background upload, e.g., we need
        // to make sure that the location auto-on/off runs.
        coroutineScope.coroutineContext.job.invokeOnCompletion { cause ->
            cleanupHandler()
        }

        // run the providers and get the locations
        coroutineScope.launch(Dispatchers.IO) {
            providers
                // launch all providers in parallel
                .map { prov -> prov.getAndSendLocation() }
                // await all providers
                .forEach { deferred -> deferred.await() }

            // finish the job once all providers have finished
            cleanupHandler()
        }
    }
}
