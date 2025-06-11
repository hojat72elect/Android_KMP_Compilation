

package com.duckduckgo.duckchat.impl.repository

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.duckchat.impl.store.DuckChatDataStore
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

interface DuckChatFeatureRepository {
    suspend fun setDuckChatUserEnabled(enabled: Boolean)
    suspend fun setShowInBrowserMenu(showDuckChat: Boolean)
    suspend fun setShowInAddressBar(showDuckChat: Boolean)

    fun observeDuckChatUserEnabled(): Flow<Boolean>
    fun observeShowInBrowserMenu(): Flow<Boolean>
    fun observeShowInAddressBar(): Flow<Boolean>

    suspend fun isDuckChatUserEnabled(): Boolean
    suspend fun shouldShowInBrowserMenu(): Boolean
    suspend fun shouldShowInAddressBar(): Boolean

    suspend fun registerOpened()
    suspend fun wasOpenedBefore(): Boolean
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(AppScope::class)
class RealDuckChatFeatureRepository @Inject constructor(
    private val duckChatDataStore: DuckChatDataStore,
) : DuckChatFeatureRepository {
    override suspend fun setDuckChatUserEnabled(enabled: Boolean) {
        duckChatDataStore.setDuckChatUserEnabled(enabled)
    }

    override suspend fun setShowInBrowserMenu(showDuckChat: Boolean) {
        duckChatDataStore.setShowInBrowserMenu(showDuckChat)
    }

    override suspend fun setShowInAddressBar(showDuckChat: Boolean) {
        duckChatDataStore.setShowInAddressBar(showDuckChat)
    }

    override fun observeDuckChatUserEnabled(): Flow<Boolean> {
        return duckChatDataStore.observeDuckChatUserEnabled()
    }

    override fun observeShowInBrowserMenu(): Flow<Boolean> {
        return duckChatDataStore.observeShowInBrowserMenu()
    }

    override fun observeShowInAddressBar(): Flow<Boolean> {
        return duckChatDataStore.observeShowInAddressBar()
    }

    override suspend fun isDuckChatUserEnabled(): Boolean {
        return duckChatDataStore.isDuckChatUserEnabled()
    }

    override suspend fun shouldShowInBrowserMenu(): Boolean {
        return duckChatDataStore.getShowInBrowserMenu()
    }

    override suspend fun shouldShowInAddressBar(): Boolean {
        return duckChatDataStore.getShowInAddressBar()
    }

    override suspend fun registerOpened() {
        duckChatDataStore.registerOpened()
    }

    override suspend fun wasOpenedBefore(): Boolean {
        return duckChatDataStore.wasOpenedBefore()
    }
}
