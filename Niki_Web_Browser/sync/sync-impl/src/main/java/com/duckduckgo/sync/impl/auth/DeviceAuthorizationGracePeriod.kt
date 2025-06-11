

package com.duckduckgo.sync.impl.auth

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import timber.log.Timber

/**
 * A grace period for local device authorization.
 * This is used to allow device authorization to be skipped for a short period of time after a successful authorization.
 */
interface DeviceAuthorizationGracePeriod {

    /**
     * Can be used to determine if device auth is required. If not required, it can be bypassed.
     * @return true if authorization is required, false otherwise
     */
    fun isAuthRequired(): Boolean

    /**
     * Records the timestamp of a successful device authorization
     */
    fun recordSuccessfulAuthorization()

    /**
     * Invalidates the grace period, so that the next call to [isAuthRequired] will return true
     */
    fun invalidate()
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class TimeBasedDeviceAuthorizationGracePeriod @Inject constructor(
    private val timeProvider: TimeProvider,
) : DeviceAuthorizationGracePeriod {

    private var lastSuccessfulAuthTime: Long? = null

    override fun recordSuccessfulAuthorization() {
        lastSuccessfulAuthTime = timeProvider.currentTimeMillis()
        Timber.v("Recording timestamp of successful auth")
    }

    override fun isAuthRequired(): Boolean {
        lastSuccessfulAuthTime?.let { lastAuthTime ->
            val timeSinceLastAuth = timeProvider.currentTimeMillis() - lastAuthTime
            Timber.v("Last authentication was $timeSinceLastAuth ms ago")
            if (timeSinceLastAuth <= AUTH_GRACE_PERIOD_MS) {
                Timber.v("Within grace period; auth not required")
                return false
            }
        }
        Timber.v("No last auth time recorded or outside grace period; auth required")

        return true
    }

    override fun invalidate() {
        lastSuccessfulAuthTime = null
    }

    companion object {
        private const val AUTH_GRACE_PERIOD_MS = 15_000
    }
}

interface TimeProvider {
    fun currentTimeMillis(): Long
}

@ContributesBinding(AppScope::class)
class SystemCurrentTimeProvider @Inject constructor() : TimeProvider {
    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}
