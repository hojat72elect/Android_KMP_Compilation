

package com.duckduckgo.common.utils

import kotlinx.coroutines.Job

/**
 * Job util class that will cancel the previous job if a new one is set.
 *
 * Assign the new job with the += operator.
 */
class ConflatedJob {

    private var job: Job? = null

    val isActive get() = job?.isActive ?: false

    @Synchronized
    operator fun plusAssign(newJob: Job) {
        cancel()
        job = newJob
    }

    fun cancel() {
        job?.cancel()
    }

    fun start() {
        job?.start()
    }

    suspend fun join() {
        job?.join()
    }
}
