

package com.duckduckgo.savedsites.impl.service

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.savedsites.api.SavedSitesRepository
import com.duckduckgo.sync.api.DeviceSyncState
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class SavedSitesDataCleaner @Inject constructor(
    private val savedSitesRepository: SavedSitesRepository,
    private val deviceSyncState: DeviceSyncState,
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        // https://app.asana.com/0/69071770703008/1204375817149200/f
        coroutineScope.launch(dispatcherProvider.io()) {
            if (!deviceSyncState.isUserSignedInOnDevice()) {
                savedSitesRepository.pruneDeleted()
            }
        }
    }
}
