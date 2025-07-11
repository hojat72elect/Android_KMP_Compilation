

package com.duckduckgo.anrs.api

import androidx.annotation.WorkerThread

interface CrashLogger {
    /**
     * Logs the [Crash] to be sent later on to the backend
     *
     * This method shall be executed off the main thread otherwise it will throw [IllegalStateException]
     *
     *@param crash [Crash] model
     */
    @WorkerThread
    fun logCrash(crash: Crash)

    data class Crash(
        val shortName: String,
        val t: Throwable,
    )
}
