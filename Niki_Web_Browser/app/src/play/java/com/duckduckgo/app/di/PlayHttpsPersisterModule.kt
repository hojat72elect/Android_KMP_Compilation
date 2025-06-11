

package com.duckduckgo.app.di

import android.content.Context
import com.duckduckgo.common.utils.store.BinaryDataStore
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.httpsupgrade.api.HttpsEmbeddedDataPersister
import com.duckduckgo.httpsupgrade.impl.HttpsDataPersister
import com.duckduckgo.httpsupgrade.impl.di.HttpsPersisterModule
import com.duckduckgo.httpsupgrade.store.HttpsBloomFilterSpecDao
import com.duckduckgo.httpsupgrade.store.PlayHttpsEmbeddedDataPersister
import com.squareup.anvil.annotations.ContributesTo
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides

@Module
@ContributesTo(
    scope = AppScope::class,
    replaces = [HttpsPersisterModule::class],
)
class PlayHttpsPersisterModule {

    @Provides
    fun providesPlayHttpsEmbeddedDataPersister(
        httpsDataPersister: HttpsDataPersister,
        binaryDataStore: BinaryDataStore,
        httpsBloomSpecDao: HttpsBloomFilterSpecDao,
        context: Context,
        moshi: Moshi,
    ): HttpsEmbeddedDataPersister {
        return PlayHttpsEmbeddedDataPersister(httpsDataPersister, binaryDataStore, httpsBloomSpecDao, context, moshi)
    }
}
