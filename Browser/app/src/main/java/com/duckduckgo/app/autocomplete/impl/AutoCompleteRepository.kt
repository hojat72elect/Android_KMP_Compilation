

package com.duckduckgo.app.autocomplete.impl

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface AutoCompleteRepository {

    suspend fun countHistoryInAutoCompleteIAMShown(): Int
    suspend fun dismissHistoryInAutoCompleteIAM()
    suspend fun wasHistoryInAutoCompleteIAMDismissed(): Boolean
    suspend fun submitUserSeenHistoryIAM()
}

@ContributesBinding(AppScope::class)
class RealAutoCompleteRepository @Inject constructor(
    private val dataStore: AutoCompleteDataStore,
    private val dispatcherProvider: DispatcherProvider,
) : AutoCompleteRepository {

    override suspend fun countHistoryInAutoCompleteIAMShown(): Int {
        return withContext(dispatcherProvider.io()) {
            dataStore.countHistoryInAutoCompleteIAMShown()
        }
    }

    override suspend fun dismissHistoryInAutoCompleteIAM() {
        withContext(dispatcherProvider.io()) {
            dataStore.setHistoryInAutoCompleteIAMDismissed()
        }
    }

    override suspend fun wasHistoryInAutoCompleteIAMDismissed(): Boolean {
        return withContext(dispatcherProvider.io()) {
            dataStore.wasHistoryInAutoCompleteIAMDismissed()
        }
    }

    override suspend fun submitUserSeenHistoryIAM() {
        withContext(dispatcherProvider.io()) {
            dataStore.incrementCountHistoryInAutoCompleteIAMShown()
        }
    }
}
