package ca.hojat.gamehub.core.data.api.igdb.common

import ca.hojat.gamehub.core.data.api.igdb.auth.entities.ApiOauthCredentials

interface CredentialsStore {
    suspend fun saveOauthCredentials(oauthCredentials: ApiOauthCredentials)
    suspend fun getLocalOauthCredentials(): ApiOauthCredentials?
    suspend fun getRemoteOauthCredentials(): ApiOauthCredentials?
}
