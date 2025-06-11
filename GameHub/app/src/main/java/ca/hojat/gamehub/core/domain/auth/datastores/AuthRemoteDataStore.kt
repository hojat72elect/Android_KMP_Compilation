package ca.hojat.gamehub.core.domain.auth.datastores

import ca.hojat.gamehub.core.domain.auth.entities.OauthCredentials
import ca.hojat.gamehub.core.domain.DomainResult

interface AuthRemoteDataStore {
    suspend fun getOauthCredentials(): DomainResult<OauthCredentials>
}
