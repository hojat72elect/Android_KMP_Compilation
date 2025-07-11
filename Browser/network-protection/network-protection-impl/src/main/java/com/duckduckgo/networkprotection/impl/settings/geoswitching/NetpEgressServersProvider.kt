

package com.duckduckgo.networkprotection.impl.settings.geoswitching

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.networkprotection.impl.configuration.EligibleLocation
import com.duckduckgo.networkprotection.impl.settings.geoswitching.NetpEgressServersProvider.PreferredLocation
import com.duckduckgo.networkprotection.impl.settings.geoswitching.NetpEgressServersProvider.ServerLocation
import com.duckduckgo.networkprotection.store.NetPGeoswitchingRepository
import com.duckduckgo.networkprotection.store.db.NetPGeoswitchingLocation
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface NetpEgressServersProvider {
    suspend fun updateServerLocationsAndReturnPreferred(eligibleLocations: List<EligibleLocation>): PreferredLocation?
    suspend fun getServerLocations(): List<ServerLocation>

    data class ServerLocation(
        val countryCode: String,
        val countryName: String,
        val cities: List<String>,
    )

    data class PreferredLocation(
        val countryCode: String,
        val cityName: String? = null,
    )
}

@ContributesBinding(AppScope::class)
class RealNetpEgressServersProvider @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val netPGeoswitchingRepository: NetPGeoswitchingRepository,
) : NetpEgressServersProvider {
    override suspend fun updateServerLocationsAndReturnPreferred(eligibleLocations: List<EligibleLocation>): PreferredLocation? = withContext(
        dispatcherProvider.io(),
    ) {
        val serverLocations = eligibleLocations
            .map { location ->
                NetPGeoswitchingLocation(
                    countryCode = location.country,
                    countryName = getDisplayableCountry(location.country),
                    cities = location.cities.map { it.name },
                )
            }.toList()
        netPGeoswitchingRepository.replaceLocations(serverLocations)
        val (selectedCountry, selectedCityName) = netPGeoswitchingRepository.getUserPreferredLocation()

        if (selectedCountry != null) {
            if (selectedCityName != null) {
                val isPresent = serverLocations.asSequence()
                    .filter { it.countryCode == selectedCountry }
                    .flatMap { it.cities }
                    .contains(selectedCityName)

                if (isPresent) {
                    // previously selected server location still exists in updated server list
                    return@withContext PreferredLocation(selectedCountry, selectedCityName)
                } else {
                    val isCountryPresent = serverLocations.asSequence().map { it.countryCode }.contains(selectedCountry)
                    if (isCountryPresent) {
                        // previously selected server city location is no longer in updated server list
                        return@withContext PreferredLocation(selectedCountry)
                    } else {
                        return@withContext null
                    }
                }
            } else {
                val isPresent = serverLocations.map { it.countryCode }.contains(selectedCountry)
                if (isPresent) {
                    // previously selected server location still exists in updated server list
                    return@withContext PreferredLocation(selectedCountry)
                } else {
                    // previously selected server location is no longer in updated server list
                    return@withContext null
                }
            }
        } else {
            // previously selected server location is no longer in updated server list
            return@withContext null
        }
    }

    override suspend fun getServerLocations(): List<ServerLocation> = withContext(dispatcherProvider.io()) {
        netPGeoswitchingRepository.getLocations().map {
            ServerLocation(
                countryCode = it.countryCode,
                countryName = it.countryName,
                cities = it.cities,
            )
        }
    }
}
