package ca.hojat.gamehub.core.data.api.igdb.common

import ca.hojat.gamehub.BuildConfig
import javax.inject.Inject

interface TwitchConstantsProvider {
    val clientId: String
    val clientSecret: String
    val apiBaseUrl: String
}

class ProdTwitchConstantsProvider @Inject constructor() : TwitchConstantsProvider {
    override val clientId: String = BuildConfig.TWITCH_APP_CLIENT_ID
    override val clientSecret: String = BuildConfig.TWITCH_APP_CLIENT_SECRET
    override val apiBaseUrl: String = Constants.TWITCH_API_BASE_URL
}
