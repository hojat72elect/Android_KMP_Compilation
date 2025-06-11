

package com.duckduckgo.networkprotection.impl.settings.geoswitching

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.networkprotection.impl.configuration.WgServerDebugProvider
import com.duckduckgo.networkprotection.store.NetPGeoswitchingRepository
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface DisplayablePreferredLocationProvider {
    suspend fun getDisplayablePreferredLocation(): String?
}

@ContributesBinding(ActivityScope::class)
class RealDisplayablePreferredLocationProvider @Inject constructor(
    private val netPGeoswitchingRepository: NetPGeoswitchingRepository,
    private val wgServerDebugProvider: WgServerDebugProvider,
    private val dispatcherProvider: DispatcherProvider,
) : DisplayablePreferredLocationProvider {
    override suspend fun getDisplayablePreferredLocation(): String? {
        return withContext(dispatcherProvider.io()) {
            val currentUserPreferredLocation = netPGeoswitchingRepository.getUserPreferredLocation()
            return@withContext if (wgServerDebugProvider.getSelectedServerName() != null) {
                val server = wgServerDebugProvider.getSelectedServerName()!!
                val countryName = currentUserPreferredLocation.countryCode?.run {
                    getDisplayableCountry(this)
                }
                if (countryName.isNullOrEmpty()) {
                    server
                } else {
                    "$server (${currentUserPreferredLocation.cityName}, $countryName)"
                }
            } else if (!currentUserPreferredLocation.countryCode.isNullOrEmpty()) {
                if (!currentUserPreferredLocation.cityName.isNullOrEmpty()) {
                    "${currentUserPreferredLocation.cityName!!}, ${getDisplayableCountry(currentUserPreferredLocation.countryCode!!)}"
                } else {
                    getDisplayableCountry(currentUserPreferredLocation.countryCode!!)
                }
            } else {
                null
            }
        }
    }
}
