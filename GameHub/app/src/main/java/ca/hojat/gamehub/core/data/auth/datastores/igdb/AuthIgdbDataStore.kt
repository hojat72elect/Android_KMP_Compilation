package ca.hojat.gamehub.core.data.auth.datastores.igdb

import ca.hojat.gamehub.core.domain.DomainResult
import ca.hojat.gamehub.core.domain.auth.datastores.AuthRemoteDataStore
import ca.hojat.gamehub.core.domain.auth.entities.OauthCredentials
import ca.hojat.gamehub.core.data.api.ApiErrorMapper
import ca.hojat.gamehub.core.data.api.igdb.auth.AuthEndpoint
import com.github.michaelbull.result.mapEither
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
class AuthIgdbDataStore @Inject constructor(
    private val authEndpoint: AuthEndpoint,
    private val igdbAuthMapper: IgdbAuthMapper,
    private val apiErrorMapper: ApiErrorMapper,
) : AuthRemoteDataStore {

    override suspend fun getOauthCredentials(): DomainResult<OauthCredentials> {
        return authEndpoint
            .getOauthCredentials()
            .mapEither(
                igdbAuthMapper::mapToDomainOauthCredentials,
                apiErrorMapper::mapToDomainError
            )
    }
}
