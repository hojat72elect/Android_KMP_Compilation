

package com.duckduckgo.app.statistics

interface AtbInitializerListener {

    /** This method will be called before initializing the ATB */
    suspend fun beforeAtbInit()

    /** @return the timeout in milliseconds after which [beforeAtbInit] will be stopped */
    fun beforeAtbInitTimeoutMillis(): Long

    companion object {
        const val PRIORITY_REINSTALL_LISTENER = 10
        const val PRIORITY_AURA_EXPERIMENT_MANAGER = 20
    }
}
