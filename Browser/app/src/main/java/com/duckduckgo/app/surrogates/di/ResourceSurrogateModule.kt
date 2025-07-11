

package com.duckduckgo.app.surrogates.di

import com.duckduckgo.app.surrogates.ResourceSurrogates
import com.duckduckgo.app.surrogates.ResourceSurrogatesImpl
import com.duckduckgo.di.scopes.AppScope
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
class ResourceSurrogateModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun analyticsSurrogates(): ResourceSurrogates = ResourceSurrogatesImpl()
}
