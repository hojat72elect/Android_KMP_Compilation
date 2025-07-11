

package com.duckduckgo.sync.impl.di

import com.duckduckgo.di.*
import com.duckduckgo.di.scopes.*
import com.duckduckgo.sync.api.*
import com.squareup.anvil.annotations.*
import dagger.*
import dagger.multibindings.*

@Module
@ContributesTo(ActivityScope::class)
abstract class SyncMessageModule {
    // we use multibinds as the list of plugins can be empty
    @Multibinds
    abstract fun provideSyncMessagePlugins(): DaggerSet<SyncMessagePlugin>
}
