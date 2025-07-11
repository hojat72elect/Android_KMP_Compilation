

package com.duckduckgo.networkprotection.impl

import com.duckduckgo.di.scopes.VpnScope
import com.squareup.anvil.annotations.ContributesBinding
import java.time.Instant
import javax.inject.Inject

interface CurrentTimeProvider {
    fun getTimeInMillis(): Long
    fun getTimeInEpochSeconds(): Long
}

@ContributesBinding(VpnScope::class)
class RealCurrentTimeProvider @Inject constructor() : CurrentTimeProvider {
    override fun getTimeInMillis(): Long = System.currentTimeMillis()
    override fun getTimeInEpochSeconds(): Long = Instant.now().epochSecond
}
