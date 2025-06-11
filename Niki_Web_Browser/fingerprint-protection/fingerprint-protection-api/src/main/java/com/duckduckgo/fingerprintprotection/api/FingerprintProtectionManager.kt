

package com.duckduckgo.fingerprintprotection.api

/** Public interface for the Fingerprint Protection feature */
interface FingerprintProtectionManager {

    /**
     * Gets the current random seed.
     */
    fun getSeed(): String
}
