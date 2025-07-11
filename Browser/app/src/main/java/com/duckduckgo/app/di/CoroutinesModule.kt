

package com.duckduckgo.app.di

import com.duckduckgo.common.utils.DefaultDispatcherProvider
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
object CoroutinesModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesDispatcherProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }
}
