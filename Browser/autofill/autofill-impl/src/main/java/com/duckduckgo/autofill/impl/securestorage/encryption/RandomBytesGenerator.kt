

package com.duckduckgo.securestorage.impl.encryption

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.security.SecureRandom
import javax.inject.Inject

/**
 * This class is responsible for generating new ByteArray passwords/passphrase that can be used for any purpose
 */
interface RandomBytesGenerator {
    fun generateBytes(size: Int): ByteArray
}

@ContributesBinding(AppScope::class)
class RealRandomBytesGenerator @Inject constructor() : RandomBytesGenerator {

    override fun generateBytes(size: Int): ByteArray {
        return ByteArray(size).apply {
            SecureRandom.getInstanceStrong().nextBytes(this)
        }
    }
}
