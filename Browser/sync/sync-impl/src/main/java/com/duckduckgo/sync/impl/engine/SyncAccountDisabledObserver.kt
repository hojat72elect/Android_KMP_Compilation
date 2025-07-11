

package com.duckduckgo.sync.impl.engine

import androidx.lifecycle.*
import com.duckduckgo.app.di.*
import com.duckduckgo.app.lifecycle.*
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.*
import com.duckduckgo.sync.api.engine.*
import com.duckduckgo.sync.store.*
import com.squareup.anvil.annotations.*
import javax.inject.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class SyncAccountDisabledObserver @Inject constructor(
    @AppCoroutineScope val appCoroutineScope: CoroutineScope,
    private val syncStore: SyncStore,
    private val syncEngine: SyncEngine,
    private val dispatcherProvider: DispatcherProvider,
) : MainProcessLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        syncStore.isSignedInFlow()
            .drop(1) // we only want to listen for changes here
            .distinctUntilChanged()
            .onEach { signedIn ->
                if (!signedIn) {
                    Timber.i("Sync disabled, notify engine")
                    syncEngine.onSyncDisabled()
                }
            }
            .flowOn(dispatcherProvider.io())
            .launchIn(appCoroutineScope)
    }
}
