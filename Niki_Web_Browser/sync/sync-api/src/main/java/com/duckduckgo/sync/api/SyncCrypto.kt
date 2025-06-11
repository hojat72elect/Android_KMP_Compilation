

package com.duckduckgo.sync.api

/** Public interface to encrypt and decrypt Sync related data*/
interface SyncCrypto {

    /**
     * Encrypts a blob of text
     * @param text to encrypt
     * @return text encrypted
     */
    fun encrypt(text: String): String

    /**
     * Decrypts a blob of text
     * @param text to decrypt
     * @return text decrypted
     */
    fun decrypt(data: String): String
}
