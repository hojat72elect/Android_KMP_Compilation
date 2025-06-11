

package com.duckduckgo.subscriptions.impl.auth2

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

interface PkceGenerator {
    fun generateCodeVerifier(): String
    fun generateCodeChallenge(codeVerifier: String): String
}

@ContributesBinding(AppScope::class)
class PkceGeneratorImpl @Inject constructor() : PkceGenerator {

    override fun generateCodeVerifier(): String {
        val code = ByteArray(32)
            .apply { SecureRandom().nextBytes(this) }

        return code.encodeBase64()
    }

    override fun generateCodeChallenge(codeVerifier: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(codeVerifier.toByteArray(Charsets.US_ASCII))
            .encodeBase64()
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun ByteArray.encodeBase64(): String {
        return Base64.UrlSafe.encode(this).trimEnd('=')
    }
}
