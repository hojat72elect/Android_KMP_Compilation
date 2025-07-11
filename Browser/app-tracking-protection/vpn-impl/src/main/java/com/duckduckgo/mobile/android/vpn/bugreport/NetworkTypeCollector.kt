

package com.duckduckgo.mobile.android.vpn.bugreport

import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.bugreport.NetworkTypeMonitor.Companion
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollectorPlugin
import com.frybits.harmony.getHarmonySharedPreferences
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.Moshi
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.withContext
import org.json.JSONObject

@ContributesMultibinding(ActivityScope::class)
class NetworkTypeCollector @Inject constructor(
    private val context: Context,
    private val dispatcherProvider: DispatcherProvider,
) : VpnStateCollectorPlugin {
    private val adapter = Moshi.Builder().build().adapter(NetworkInfo::class.java)
    private val preferences: SharedPreferences
        get() = context.getHarmonySharedPreferences(NetworkTypeMonitor.FILENAME)

    override val collectorName = "networkInfo"

    override suspend fun collectVpnRelatedState(appPackageId: String?): JSONObject = withContext(dispatcherProvider.io()) {
        return@withContext getNetworkInfoJsonObject()
    }

    private fun getNetworkInfoJsonObject(): JSONObject {
        // redact the lastSwitchTimestampMillis from the report
        val info = getCurrentNetworkInfo()?.let {
            // Redact some values (set to -999) as they could be static values
            val temp = adapter.fromJson(it)
            adapter.toJson(
                temp?.copy(
                    lastSwitchTimestampMillis = -999,
                    currentNetwork = temp.currentNetwork.copy(netId = -999),
                    previousNetwork = temp.previousNetwork?.copy(netId = -999),
                    secondsSinceLastSwitch = TimeUnit.MILLISECONDS.toSeconds(SystemClock.elapsedRealtime() - temp.lastSwitchTimestampMillis),
                ),
            )
        } ?: return JSONObject()

        return JSONObject(info)
    }

    private fun getCurrentNetworkInfo(): String? {
        return preferences.getString(Companion.NETWORK_INFO_KEY, null)
    }

    internal data class NetworkInfo(
        val currentNetwork: Connection,
        val previousNetwork: Connection? = null,
        val lastSwitchTimestampMillis: Long,
        val secondsSinceLastSwitch: Long,
    )

    internal data class Connection(
        val netId: Long,
        val type: NetworkType,
        val state: NetworkState,
        val mnc: Int? = null,
    )

    internal enum class NetworkType {
        WIFI,
        CELLULAR,
    }

    internal enum class NetworkState {
        AVAILABLE,
        LOST,
    }
}
