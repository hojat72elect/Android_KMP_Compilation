

package com.duckduckgo.autofill.impl.securestorage

import java.security.Key
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

interface DerivedKeySecretFactory {
    fun getKey(spec: PBEKeySpec): Key
}

class RealDerivedKeySecretFactory : DerivedKeySecretFactory {
    private val secretKeyFactory by lazy {
        SecretKeyFactory.getInstance("PBKDF2withHmacSHA256")
    }

    override fun getKey(spec: PBEKeySpec): Key = secretKeyFactory.generateSecret(spec)
}
