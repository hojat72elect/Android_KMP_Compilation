package ca.hojat.gamehub.core.data.auth.datastores.file

import ca.hojat.gamehub.core.domain.auth.entities.OauthCredentials
import javax.inject.Inject

class ProtoAuthMapper @Inject constructor(
    private val authExpiryTimeCalculator: AuthExpiryTimeCalculator
) {

    fun mapToProtoOauthCredentials(oauthCredentials: OauthCredentials): NewProtoOauthCredentials {
        return NewProtoOauthCredentials.newBuilder()
            .setAccessToken(oauthCredentials.accessToken)
            .setTokenType(oauthCredentials.tokenType)
            .setTokenTtl(oauthCredentials.tokenTtl)
            .setExpirationTime(authExpiryTimeCalculator.calculateExpiryTime(oauthCredentials))
            .build()
    }

    fun mapToDomainOauthCredentials(oauthCredentials: NewProtoOauthCredentials): OauthCredentials {
        return OauthCredentials(
            accessToken = oauthCredentials.accessToken,
            tokenType = oauthCredentials.tokenType,
            tokenTtl = oauthCredentials.tokenTtl
        )
    }
}
