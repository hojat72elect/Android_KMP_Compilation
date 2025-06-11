

package com.duckduckgo.autofill.impl.ui.credential.saving.declines

import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.autofill.impl.store.InternalAutofillStore
import com.duckduckgo.common.utils.ConflatedJob
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class DisablePromptCredentialsObserver @Inject constructor(
    private val internalAutofillStore: InternalAutofillStore,
    private val autofillDeclineCounter: AutofillDeclineCounter,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatchers: DispatcherProvider,
) : MainProcessLifecycleObserver {

    private val observerJob = ConflatedJob()

    override fun onCreate(owner: LifecycleOwner) {
        appCoroutineScope.launch(dispatchers.io()) {
            if (internalAutofillStore.autofillAvailable() && autofillDeclineCounter.isDeclineCounterActive()) {
                observerJob += internalAutofillStore.getCredentialCount().onEach { credentialsCount ->
                    if (credentialsCount > 0) {
                        autofillDeclineCounter.disableDeclineCounter()
                        observerJob.cancel()
                    }
                }.flowOn(dispatchers.io()).launchIn(appCoroutineScope)
            }
        }
    }
}
