

package com.duckduckgo.httpsupgrade.api

interface HttpsEmbeddedDataPersister {

    fun shouldPersistEmbeddedData(): Boolean

    fun persistEmbeddedData()
}
