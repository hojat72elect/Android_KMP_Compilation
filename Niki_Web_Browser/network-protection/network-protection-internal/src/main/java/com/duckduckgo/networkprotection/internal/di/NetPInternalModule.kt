

package com.duckduckgo.networkprotection.internal.di

import android.content.Context
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.networkprotection.store.remote_config.*
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
@ContributesTo(AppScope::class)
object NetPInternalModule {

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideNetPInternalConfigDatabase(context: Context): NetPInternalConfigDatabase {
        return NetPInternalConfigDatabase.create(context)
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    @InternalNetPConfigTogglesDao
    fun provideNetPConfigTogglesDao(netPInternalConfigDatabase: NetPInternalConfigDatabase): NetPConfigTogglesDao {
        return netPInternalConfigDatabase.configTogglesDao()
    }

    @SingleInstanceIn(AppScope::class)
    @Provides
    fun provideNetPServersDao(netPInternalConfigDatabase: NetPInternalConfigDatabase): NetPServersDao {
        return netPInternalConfigDatabase.serversDao()
    }

    @Provides
    fun provideNetPServerRepository(netPServersDao: NetPServersDao): NetPServerRepository {
        return NetPServerRepository(netPServersDao)
    }
}
