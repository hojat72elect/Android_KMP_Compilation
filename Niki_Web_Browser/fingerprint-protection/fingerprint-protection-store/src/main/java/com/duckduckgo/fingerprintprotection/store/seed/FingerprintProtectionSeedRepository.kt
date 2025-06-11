

package com.duckduckgo.fingerprintprotection.store.seed

import java.util.*
import java.util.concurrent.atomic.AtomicReference

interface FingerprintProtectionSeedRepository {
    val seed: String
    fun storeNewSeed()
}

class RealFingerprintProtectionSeedRepository constructor() : FingerprintProtectionSeedRepository {

    private val atomicSeed = AtomicReference(getRandomSeed())
    override val seed: String
        get() = atomicSeed.get()

    override fun storeNewSeed() {
        atomicSeed.set(getRandomSeed())
    }

    private fun getRandomSeed(): String {
        return UUID.randomUUID().toString()
    }
}
