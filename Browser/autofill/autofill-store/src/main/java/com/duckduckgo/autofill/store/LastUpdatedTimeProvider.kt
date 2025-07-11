

package com.duckduckgo.autofill.store

interface LastUpdatedTimeProvider {
    fun getInMillis(): Long
}

class RealLastUpdatedTimeProvider : LastUpdatedTimeProvider {
    override fun getInMillis(): Long = System.currentTimeMillis()
}
