package ca.hojat.gamehub.core.data.auth.datastores.igdb

import ca.hojat.gamehub.core.data.api.igdb.auth.entities.ApiOauthCredentials
import ca.hojat.gamehub.core.data.api.igdb.common.CredentialsStore
import ca.hojat.gamehub.core.domain.auth.datastores.AuthLocalDataStore
import ca.hojat.gamehub.core.domain.auth.datastores.AuthRemoteDataStore
import com.github.michaelbull.result.get
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
class CredentialsStoreImpl @Inject constructor(
    private val authLocalDataStore: AuthLocalDataStore,
    private val authRemoteDataStore: AuthRemoteDataStore,
    private val igdbAuthMapper: IgdbAuthMapper,
) : CredentialsStore {

    override suspend fun saveOauthCredentials(oauthCredentials: ApiOauthCredentials) {
        authLocalDataStore.saveOauthCredentials(
            igdbAuthMapper.mapToDomainOauthCredentials(oauthCredentials),
        )
    }

    override suspend fun getLocalOauthCredentials(): ApiOauthCredentials? {
        return authLocalDataStore.getOauthCredentials()
            ?.let(igdbAuthMapper::mapToApiOauthCredentials)
    }

    override suspend fun getRemoteOauthCredentials(): ApiOauthCredentials? {
        return authRemoteDataStore.getOauthCredentials()
            .get()
            ?.let(igdbAuthMapper::mapToApiOauthCredentials)
    }
}
