

package com.duckduckgo.autofill.impl.engagement

import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.autofill.impl.engagement.store.AutofillEngagementRepository
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesPluginPoint(AppScope::class)
interface DataAutofilledListener {
    fun onAutofilledSavedPassword()
    fun onAutofilledDuckAddress()
    fun onUsedGeneratedPassword()
}

@ContributesMultibinding(AppScope::class)
class DefaultDataAutofilledListener @Inject constructor(
    private val engagementRepository: AutofillEngagementRepository,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatchers: DispatcherProvider,
) : DataAutofilledListener {

    override fun onAutofilledSavedPassword() {
        Timber.v("onAutofilledSavedPassword, recording autofilled today")
        recordAutofilledToday()
    }

    override fun onAutofilledDuckAddress() {
        Timber.v("onAutofilledDuckAddress, recording autofilled today")
        recordAutofilledToday()
    }

    override fun onUsedGeneratedPassword() {
        Timber.v("onUsedGeneratedPassword, recording autofilled today")
        recordAutofilledToday()
    }

    private fun recordAutofilledToday() {
        appCoroutineScope.launch(dispatchers.io()) {
            engagementRepository.recordAutofilledToday()
        }
    }
}
