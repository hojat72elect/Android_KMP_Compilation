

package com.duckduckgo.cookies.store

import android.content.Context

interface CookiesFeatureToggleRepository : CookiesFeatureToggleStore {
    companion object {
        fun create(
            context: Context,
        ): CookiesFeatureToggleRepository {
            val store = RealCookiesFeatureToggleStore(context)
            return RealCookiesFeatureToggleRepository(store)
        }
    }
}

class RealCookiesFeatureToggleRepository constructor(
    private val cookiesFeatureToggleStore: CookiesFeatureToggleStore,
) : CookiesFeatureToggleRepository, CookiesFeatureToggleStore by cookiesFeatureToggleStore
