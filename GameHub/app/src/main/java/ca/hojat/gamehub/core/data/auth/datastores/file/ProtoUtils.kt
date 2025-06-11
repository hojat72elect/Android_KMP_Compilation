package ca.hojat.gamehub.core.data.auth.datastores.file

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

fun NewProtoOauthCredentials.isNotEmpty(): Boolean {
    return (
            accessToken.isNotEmpty() &&
                    tokenType.isNotEmpty() &&
                    (tokenTtl != 0L) &&
                    (expirationTime != 0L)
            )
}

object ProtoOauthCredentialsSerializer : Serializer<NewProtoOauthCredentials> {

    override val defaultValue: NewProtoOauthCredentials
        get() = NewProtoOauthCredentials.newBuilder()
            .setAccessToken("")
            .setTokenType("")
            .setTokenTtl(0L)
            .setExpirationTime(0L)
            .build()

    override suspend fun writeTo(t: NewProtoOauthCredentials, output: OutputStream) =
        t.writeTo(output)

    override suspend fun readFrom(input: InputStream): NewProtoOauthCredentials =
        NewProtoOauthCredentials.parseFrom(input)
}
