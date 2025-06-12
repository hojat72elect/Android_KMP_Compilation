package de.nulide.findmydevice.locationproviders

import android.content.Context
import de.nulide.findmydevice.R
import de.nulide.findmydevice.data.Settings
import de.nulide.findmydevice.data.SettingsRepository
import de.nulide.findmydevice.net.OpenCelliDRepository
import de.nulide.findmydevice.net.OpenCelliDSpec
import de.nulide.findmydevice.transports.Transport
import de.nulide.findmydevice.utils.CellParameters
import de.nulide.findmydevice.utils.log
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import java.util.Calendar
import java.util.TimeZone


/**
 * Only call this provider via the LocateCommand!
 * (because it handles things like LocationAutoOnOff centrally)
 */
class CellLocationProvider<T>(
    private val context: Context,
    private val transport: Transport<T>,
) : LocationProvider() {

    companion object {
        private val TAG = CellLocationProvider::class.simpleName
    }

    override fun getAndSendLocation(): Deferred<Unit> {
        val deferred = CompletableDeferred<Unit>()

        val settings = SettingsRepository.getInstance(context)
        val apiAccessToken = settings.get(Settings.SET_OPENCELLID_API_KEY) as String
        if (apiAccessToken.isEmpty()) {
            val msg = "Cannot send cell location: Missing API Token"
            context.log().i(TAG, msg)
            transport.send(context, msg)
            deferred.complete(Unit)
            return deferred
        }

        val paras = CellParameters.queryCellParametersFromTelephonyManager(context)
        if (paras == null) {
            context.log().i(TAG, "Cell paras are null. Are you connected to the cellular network?")
            transport.send(context, context.getString(R.string.OpenCellId_test_no_connection))
            deferred.complete(Unit)
            return deferred
        }

        val repo = OpenCelliDRepository.getInstance(OpenCelliDSpec(context))

        context.log().d(TAG, "Requesting location from OpenCelliD")
        repo.getCellLocation(
            paras, apiAccessToken,
            onSuccess = {
                context.log().d(TAG, "Location found by OpenCelliD")
                val timeMillis = Calendar.getInstance(TimeZone.getTimeZone("UTC")).timeInMillis
                storeLastKnownLocation(context, it.lat, it.lon, timeMillis)
                transport.sendNewLocation(context, "OpenCelliD", it.lat, it.lon, timeMillis)
                deferred.complete(Unit)
            },
            onError = {
                context.log().i(TAG, "Failed to get location from OpenCelliD")
                val msg = context.getString(
                    R.string.cmd_locate_response_opencellid_failed,
                    it.url,
                    paras.prettyPrint()
                )
                transport.send(context, msg)
                deferred.complete(Unit)
            },
        )
        return deferred
    }
}
