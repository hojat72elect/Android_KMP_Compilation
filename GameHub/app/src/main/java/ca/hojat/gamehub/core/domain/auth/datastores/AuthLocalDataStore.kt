package ca.hojat.gamehub.core.domain.auth.datastores

import ca.hojat.gamehub.core.domain.auth.entities.OauthCredentials

interface AuthLocalDataStore {
    suspend fun saveOauthCredentials(oauthCredentials: OauthCredentials)
    suspend fun getOauthCredentials(): OauthCredentials?
}
