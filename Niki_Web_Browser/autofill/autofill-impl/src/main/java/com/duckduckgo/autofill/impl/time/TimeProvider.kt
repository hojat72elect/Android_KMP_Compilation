

package com.duckduckgo.autofill.impl.time

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface TimeProvider {
    fun currentTimeMillis(): Long
}

@ContributesBinding(AppScope::class)
class SystemCurrentTimeProvider @Inject constructor() : TimeProvider {
    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}
