

package com.duckduckgo.pir.internal.brokers

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.pir.internal.PirRemoteFeatures
import com.duckduckgo.pir.internal.store.PitTestingStore
import com.duckduckgo.subscriptions.api.Subscriptions
import com.squareup.anvil.annotations.ContributesMultibinding
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import logcat.logcat

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class PirDataUpdateObserver @Inject constructor(
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    private val brokerJsonUpdater: BrokerJsonUpdater,
    private val subscriptions: Subscriptions,
    private val pirRemoteFeatures: PirRemoteFeatures,
    private val testingStore: PitTestingStore,
) : MainProcessLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (pirRemoteFeatures.allowPirRun().isEnabled() && subscriptions.getAccessToken() != null) {
                if (testingStore.testerId == null) {
                    testingStore.testerId = UUID.randomUUID().toString()
                }
                logcat { "PIR-update: Attempting to update all broker data" }
                if (brokerJsonUpdater.update()) {
                    logcat { "PIR-update: Update successfully completed." }
                } else {
                    logcat { "PIR-update: Failed to complete." }
                }
            }
        }
    }
}
