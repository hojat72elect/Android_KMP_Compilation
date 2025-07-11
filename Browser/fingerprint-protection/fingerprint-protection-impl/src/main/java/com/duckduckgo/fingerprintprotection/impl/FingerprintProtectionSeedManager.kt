

package com.duckduckgo.fingerprintprotection.impl

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.fingerprintprotection.api.FingerprintProtectionManager
import com.duckduckgo.fingerprintprotection.store.seed.FingerprintProtectionSeedRepository
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class FingerprintProtectionSeedManager @Inject constructor(
    private val fingerprintProtectionSeedRepository: FingerprintProtectionSeedRepository,
) : FingerprintProtectionManager {

    override fun getSeed(): String {
        return fingerprintProtectionSeedRepository.seed
    }
}
