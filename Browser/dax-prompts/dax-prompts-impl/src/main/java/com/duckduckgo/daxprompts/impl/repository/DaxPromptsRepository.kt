

package com.duckduckgo.daxprompts.impl.repository

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.daxprompts.impl.store.DaxPromptsDataStore
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface DaxPromptsRepository {
    suspend fun setDaxPromptsShowDuckPlayer(show: Boolean)
    suspend fun getDaxPromptsShowDuckPlayer(): Boolean
    suspend fun setDaxPromptsShowBrowserComparison(show: Boolean)
    suspend fun getDaxPromptsShowBrowserComparison(): Boolean
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class RealDaxPromptsRepository @Inject constructor(
    private val daxPromptsDataStore: DaxPromptsDataStore,
    private val dispatchers: DispatcherProvider,
) : DaxPromptsRepository {

    private var showDuckPlayer: Boolean? = null
    private var showBrowserComparison: Boolean? = null

    override suspend fun setDaxPromptsShowDuckPlayer(show: Boolean) {
        showDuckPlayer = show
        withContext(dispatchers.io()) {
            daxPromptsDataStore.setDaxPromptsShowDuckPlayer(show)
        }
    }

    override suspend fun getDaxPromptsShowDuckPlayer(): Boolean {
        return showDuckPlayer ?: daxPromptsDataStore.getDaxPromptsShowDuckPlayer()
    }

    override suspend fun setDaxPromptsShowBrowserComparison(show: Boolean) {
        showBrowserComparison = show
        withContext(dispatchers.io()) {
            daxPromptsDataStore.setDaxPromptsShowBrowserComparison(show)
        }
    }

    override suspend fun getDaxPromptsShowBrowserComparison(): Boolean {
        return showBrowserComparison ?: daxPromptsDataStore.getDaxPromptsShowBrowserComparison()
    }
}
