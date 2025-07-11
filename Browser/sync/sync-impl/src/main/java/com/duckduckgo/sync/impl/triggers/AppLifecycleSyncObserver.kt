

package com.duckduckgo.sync.impl.triggers

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.sync.api.DeviceSyncState
import com.duckduckgo.sync.api.engine.SyncEngine
import com.duckduckgo.sync.api.engine.SyncEngine.SyncTrigger.APP_OPEN
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class AppLifecycleSyncObserver @Inject constructor(
    @AppCoroutineScope val appCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    private val deviceSyncState: DeviceSyncState,
    private val syncEngine: SyncEngine,
) : MainProcessLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        appCoroutineScope.launch(dispatcherProvider.io()) {
            if (deviceSyncState.isUserSignedInOnDevice()) {
                Timber.d("Sync-Engine: App started, triggering sync")
                syncEngine.triggerSync(APP_OPEN)
            }
        }
    }
}
