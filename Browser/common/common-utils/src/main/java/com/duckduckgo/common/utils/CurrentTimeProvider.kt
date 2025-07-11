

package com.duckduckgo.common.utils

import android.os.SystemClock
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import java.time.LocalDateTime
import javax.inject.Inject

interface CurrentTimeProvider {
    fun elapsedRealtime(): Long

    fun currentTimeMillis(): Long

    fun localDateTimeNow(): LocalDateTime
}

@ContributesBinding(AppScope::class)
class RealCurrentTimeProvider @Inject constructor() : CurrentTimeProvider {
    override fun elapsedRealtime(): Long = SystemClock.elapsedRealtime()

    override fun currentTimeMillis(): Long = System.currentTimeMillis()

    override fun localDateTimeNow(): LocalDateTime = LocalDateTime.now()
}
