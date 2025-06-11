package ca.hojat.gamehub.core.data.auth.datastores.file

import ca.hojat.gamehub.core.providers.TimestampProvider
import ca.hojat.gamehub.core.domain.auth.entities.OauthCredentials
import java.util.concurrent.TimeUnit
import javax.inject.Inject

val AUTH_TOKEN_TTL_DEDUCTION = TimeUnit.DAYS.toMillis(@Suppress("MagicNumber") 7)

class AuthExpiryTimeCalculator @Inject constructor(
    private val timestampProvider: TimestampProvider
) {

    fun calculateExpiryTime(oauthCredentials: OauthCredentials): Long {
        val currentTimestamp = timestampProvider.getUnixTimestamp()
        val tokenTtlInMillis = TimeUnit.SECONDS.toMillis(oauthCredentials.tokenTtl)

        return (currentTimestamp + tokenTtlInMillis - AUTH_TOKEN_TTL_DEDUCTION)
    }
}
