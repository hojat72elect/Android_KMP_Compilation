

package com.duckduckgo.httpsupgrade.impl.di

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.httpsupgrade.api.HttpsEmbeddedDataPersister
import com.duckduckgo.httpsupgrade.impl.EmptyHttpsEmbeddedDataPersister
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides

@Module
@ContributesTo(AppScope::class)
object HttpsPersisterModule {

    @Provides
    fun providesHttpsDataManager(): HttpsEmbeddedDataPersister {
        return EmptyHttpsEmbeddedDataPersister()
    }
}
