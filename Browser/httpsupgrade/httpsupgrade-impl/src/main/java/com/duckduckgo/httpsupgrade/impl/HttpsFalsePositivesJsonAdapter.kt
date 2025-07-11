

package com.duckduckgo.httpsupgrade.impl

import com.duckduckgo.httpsupgrade.store.HttpsFalsePositiveDomain
import com.squareup.moshi.FromJson

class HttpsFalsePositivesJsonAdapter {

    @FromJson
    fun adapt(data: Map<String, List<String>>): List<HttpsFalsePositiveDomain> {
        return data.getValue("data").map { HttpsFalsePositiveDomain(it) }
    }
}
