

package com.duckduckgo.app.di

import com.duckduckgo.common.test.CoroutineTestRule
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(
    scope = AppScope::class,
    replaces = [CoroutinesModule::class],
)
class StubCoroutinesModule {

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun providesDispatcherProvider(): DispatcherProvider {
        return CoroutineTestRule().testDispatcherProvider
    }
}
