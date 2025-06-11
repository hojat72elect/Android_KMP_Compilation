package ca.hojat.gamehub.core.data.api.igdb.common

import javax.inject.Inject

interface IgdbConstantsProvider {
    val apiBaseUrl: String
}

class ProdIgdbConstantsProvider @Inject constructor() : IgdbConstantsProvider {
    override val apiBaseUrl: String = Constants.IGDB_API_BASE_URL
}
