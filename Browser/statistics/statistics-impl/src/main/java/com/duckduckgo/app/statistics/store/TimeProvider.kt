

package com.duckduckgo.app.statistics.store

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.time.Instant
import javax.inject.Inject

interface TimeProvider {
    fun getCurrentTime(): Instant
}

@ContributesBinding(AppScope::class)
class TimeProviderImpl @Inject constructor() : TimeProvider {
    override fun getCurrentTime(): Instant = Instant.now()
}
