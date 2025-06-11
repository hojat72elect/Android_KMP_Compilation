

package com.duckduckgo.httpsupgrade.impl

import com.duckduckgo.httpsupgrade.api.HttpsEmbeddedDataPersister
import logcat.logcat

internal class EmptyHttpsEmbeddedDataPersister : HttpsEmbeddedDataPersister {

    override fun shouldPersistEmbeddedData(): Boolean {
        logcat { "Ignoring, empty persister does not use embedded data" }
        return false
    }

    override fun persistEmbeddedData() {
        logcat { "Ignoring, empty persister does not use embedded data" }
    }
}
