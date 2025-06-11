

package com.duckduckgo.autofill.impl.store

import com.duckduckgo.autofill.impl.securestorage.SecureStorage
import com.duckduckgo.autofill.impl.urlmatcher.AutofillUrlMatcher
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface NeverSavedSiteRepository {
    suspend fun addToNeverSaveList(url: String)
    suspend fun clearNeverSaveList()
    suspend fun neverSaveListCount(): Flow<Int>
    suspend fun isInNeverSaveList(url: String): Boolean
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class RealNeverSavedSiteRepository @Inject constructor(
    private val autofillUrlMatcher: AutofillUrlMatcher,
    private val dispatchers: DispatcherProvider,
    private val secureStorage: SecureStorage,
) : NeverSavedSiteRepository {

    override suspend fun addToNeverSaveList(url: String) = withContext(dispatchers.io()) {
        val domainToAdd = url.extractEffectiveTldPlusOne()
        secureStorage.addToNeverSaveList(domainToAdd)
    }

    override suspend fun clearNeverSaveList() = withContext(dispatchers.io()) {
        secureStorage.clearNeverSaveList()
    }

    override suspend fun neverSaveListCount(): Flow<Int> {
        return secureStorage.neverSaveListCount()
    }

    override suspend fun isInNeverSaveList(url: String): Boolean {
        return withContext(dispatchers.io()) {
            val domainToAdd = url.extractEffectiveTldPlusOne()
            secureStorage.isInNeverSaveList(domainToAdd)
        }
    }

    private fun String.extractEffectiveTldPlusOne(): String {
        val urlParts = autofillUrlMatcher.extractUrlPartsForAutofill(this)
        return urlParts.eTldPlus1 ?: this
    }
}
