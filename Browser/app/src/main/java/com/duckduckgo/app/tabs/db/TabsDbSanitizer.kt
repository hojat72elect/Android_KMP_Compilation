

package com.duckduckgo.app.tabs.db

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.adclick.api.AdClickManager
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.tabs.model.TabRepository
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import dagger.SingleInstanceIn
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SingleInstanceIn(AppScope::class)
class TabsDbSanitizer @Inject constructor(
    private val tabRepository: TabRepository,
    private val dispatchers: DispatcherProvider,
    private val adClickManager: AdClickManager,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
) : MainProcessLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        appCoroutineScope.launch(dispatchers.main()) {
            tabRepository.getDeletableTabIds().forEach {
                adClickManager.clearTabId(it)
            }
            tabRepository.purgeDeletableTabs()
        }
    }
}
