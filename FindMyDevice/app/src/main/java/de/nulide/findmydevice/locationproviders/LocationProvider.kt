package de.nulide.findmydevice.locationproviders

import android.content.Context
import de.nulide.findmydevice.data.Settings
import de.nulide.findmydevice.data.SettingsRepository
import de.nulide.findmydevice.utils.Utils.Companion.getOpenStreetMapLink
import kotlinx.coroutines.Deferred
import java.util.Date


abstract class LocationProvider {

    /**
     * Gets the location and sends it once it is available.
     *
     * This may take some time, e.g., to acquire a new GPS lock.
     * Therefore, if you override this function, you may want to create a new
     * thread to avoid blocking the caller.
     *
     * @return A Deferred that signals that the getting the location is complete.
     */
    abstract fun getAndSendLocation(): Deferred<Unit>

    companion object {
        fun storeLastKnownLocation(context: Context, lat: String, lon: String, timeMillis: Long) {
            val settings = SettingsRepository.getInstance(context)
            settings.set<String>(Settings.SET_LAST_KNOWN_LOCATION_LAT, lat)
            settings.set<String>(Settings.SET_LAST_KNOWN_LOCATION_LON, lon)
            settings.set<Long>(Settings.SET_LAST_KNOWN_LOCATION_TIME, timeMillis)
        }

        fun buildLocationString(
            provider: String,
            lat: String,
            lon: String,
            batteryLevel: String,
            timeMillis: Long
        ): String {
            return StringBuilder().append(provider)
                .append(": Lat: ").append(lat)
                .append(" Lon: ").append(lon)
                .append("\n\n")
                .append("Time: ").append(Date(timeMillis).toString())
                .append("\n\n")
                .append("Battery: ").append(batteryLevel).append(" %")
                .append("\n\n")
                .append(getOpenStreetMapLink(lat, lon))
                .toString()
        }
    }
}
