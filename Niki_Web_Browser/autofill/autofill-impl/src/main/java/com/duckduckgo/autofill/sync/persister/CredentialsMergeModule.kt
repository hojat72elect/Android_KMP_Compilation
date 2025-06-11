

package com.duckduckgo.autofill.sync.persister

import com.duckduckgo.autofill.sync.*
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.api.engine.SyncableDataPersister.SyncConflictResolution
import com.squareup.anvil.annotations.ContributesTo
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class CredentialsMergeStrategyKey(val value: SyncConflictResolution)

@Module
@ContributesTo(AppScope::class)
object CredentialsMergeModule {

    @Provides
    @IntoMap
    @CredentialsMergeStrategyKey(SyncConflictResolution.DEDUPLICATION)
    fun provideDeduplicationStrategy(
        credentialsSync: CredentialsSync,
        credentialsSyncMapper: CredentialsSyncMapper,
        dispatchers: DispatcherProvider,
    ): CredentialsMergeStrategy = CredentialsDedupStrategy(
        credentialsSync,
        credentialsSyncMapper,
        dispatchers,
    )

    @Provides
    @IntoMap
    @CredentialsMergeStrategyKey(SyncConflictResolution.LOCAL_WINS)
    fun provideLocalWinsStrategy(
        credentialsSync: CredentialsSync,
        credentialsSyncMapper: CredentialsSyncMapper,
        dispatchers: DispatcherProvider,
    ): CredentialsMergeStrategy = CredentialsLocalWinsStrategy(
        credentialsSync,
        credentialsSyncMapper,
        dispatchers,
    )

    @Provides
    @IntoMap
    @CredentialsMergeStrategyKey(SyncConflictResolution.REMOTE_WINS)
    fun provideRemoteWinsStrategy(
        credentialsSync: CredentialsSync,
        credentialsSyncMapper: CredentialsSyncMapper,
        dispatchers: DispatcherProvider,
    ): CredentialsMergeStrategy = CredentialsRemoteWinsStrategy(
        credentialsSync,
        credentialsSyncMapper,
        dispatchers,
    )

    @Provides
    @IntoMap
    @CredentialsMergeStrategyKey(SyncConflictResolution.TIMESTAMP)
    fun provideLastModifiedWinsStrategy(
        credentialsSync: CredentialsSync,
        credentialsSyncMapper: CredentialsSyncMapper,
        dispatchers: DispatcherProvider,
    ): CredentialsMergeStrategy = CredentialsLastModifiedWinsStrategy(
        credentialsSync,
        credentialsSyncMapper,
        dispatchers,
    )
}
