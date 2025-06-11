

package com.duckduckgo.request.filterer.store

import android.content.Context

interface RequestFiltererFeatureToggleRepository : RequestFiltererFeatureToggleStore {
    companion object {
        fun create(
            context: Context,
        ): RequestFiltererFeatureToggleRepository {
            val store = RealRequestFiltererFeatureToggleStore(context)
            return RealRequestFiltererFeatureToggleRepository(store)
        }
    }
}

class RealRequestFiltererFeatureToggleRepository constructor(
    private val requestFiltererFeatureToggleStore: RequestFiltererFeatureToggleStore,
) : RequestFiltererFeatureToggleRepository, RequestFiltererFeatureToggleStore by requestFiltererFeatureToggleStore
