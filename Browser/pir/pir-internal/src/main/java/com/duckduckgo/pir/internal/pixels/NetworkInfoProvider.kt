

package com.duckduckgo.pir.internal.pixels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface NetworkInfoProvider {
    fun getCurrentNetworkInfo(): String
}

@ContributesBinding(AppScope::class)
class RealNetworkInfoProvider @Inject constructor(
    private val context: Context,
) : NetworkInfoProvider {
    private val connectivityManager: ConnectivityManager by lazy { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    override fun getCurrentNetworkInfo(): String {
        val activeNetwork = connectivityManager.activeNetwork
        val activeNetworkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return if (activeNetworkCapabilities == null || !activeNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            "NO_CONNECTION"
        } else {
            when {
                activeNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
                activeNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
                activeNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ETHERNET"
                else -> "NO_CONNECTION"
            }
        }
    }
}
