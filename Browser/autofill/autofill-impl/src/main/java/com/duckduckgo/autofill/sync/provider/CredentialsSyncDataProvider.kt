

package com.duckduckgo.autofill.sync.provider

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.appbuildconfig.api.isInternalBuild
import com.duckduckgo.autofill.sync.CredentialsSync
import com.duckduckgo.autofill.sync.CredentialsSyncStore
import com.duckduckgo.autofill.sync.SyncDateProvider
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.checkMainThread
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.api.engine.*
import com.duckduckgo.sync.api.engine.SyncableType.CREDENTIALS
import com.squareup.anvil.annotations.ContributesMultibinding
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.*
import kotlinx.coroutines.runBlocking

@ContributesMultibinding(scope = AppScope::class, boundType = SyncableDataProvider::class)
class CredentialsSyncDataProvider @Inject constructor(
    private val credentialsSyncStore: CredentialsSyncStore,
    private val credentialsSync: CredentialsSync,
    private val dispatchers: DispatcherProvider,
    private val appBuildConfig: AppBuildConfig,
) : SyncableDataProvider {
    override fun getType(): SyncableType = CREDENTIALS

    override fun getChanges(): SyncChangesRequest {
        if (appBuildConfig.isInternalBuild()) checkMainThread()
        return runBlocking(dispatchers.io()) {
            if (credentialsSyncStore.serverModifiedSince == "0") {
                credentialsSync.initMetadata()
            }
            val since = credentialsSyncStore.clientModifiedSince
            val updates = credentialsSync.getUpdatesSince(since)
            val request = formatUpdates(updates)
            return@runBlocking request
        }
    }

    private fun formatUpdates(updates: List<LoginCredentialEntry>): SyncChangesRequest {
        val modifiedSince = if (credentialsSyncStore.serverModifiedSince == "0") {
            ModifiedSince.FirstSync
        } else {
            ModifiedSince.Timestamp(credentialsSyncStore.serverModifiedSince)
        }

        return if (updates.isEmpty()) {
            SyncChangesRequest(CREDENTIALS, "", modifiedSince)
        } else {
            val credentialsUpdates = SyncCredentialsUpdates(
                updates = updates,
                modified_since = credentialsSyncStore.serverModifiedSince,
            )
            val patch = SyncCredentialsRequest(
                credentials = credentialsUpdates,
                client_timestamp = SyncDateProvider.now(),
            )
            val allDataJSON = Adapters.patchAdapter.toJson(patch)
            SyncChangesRequest(CREDENTIALS, allDataJSON, modifiedSince)
        }
    }

    private class Adapters {
        companion object {
            private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val patchAdapter: JsonAdapter<SyncCredentialsRequest> = moshi.adapter(SyncCredentialsRequest::class.java).serializeNulls()
        }
    }
}

class SyncCredentialsRequest(
    val credentials: SyncCredentialsUpdates,
    val client_timestamp: String,
)

class SyncCredentialsUpdates(
    val updates: List<LoginCredentialEntry>,
    val modified_since: String = "0",
)

data class LoginCredentialEntry(
    val id: String,
    val deleted: String? = null,
    val client_last_modified: String?,
    val domain: String? = null,
    val title: String? = null,
    val username: String? = null,
    val password: String? = null,
    val notes: String? = null,
)
