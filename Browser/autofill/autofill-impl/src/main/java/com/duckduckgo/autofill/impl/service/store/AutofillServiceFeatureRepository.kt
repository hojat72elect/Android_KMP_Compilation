

package com.duckduckgo.autofill.impl.service.store

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.app.di.ProcessName
import com.duckduckgo.autofill.impl.service.AutofillServiceFeature
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyConfigCallbackPlugin
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

interface AutofillServiceFeatureRepository {
    val exceptions: CopyOnWriteArrayList<String>
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(
    scope = AppScope::class,
    boundType = AutofillServiceFeatureRepository::class,
)
@ContributesMultibinding(
    scope = AppScope::class,
    boundType = AutofillServiceFeatureRepository::class,
)
class RealAutofillServiceFeatureRepository @Inject constructor(
    @IsMainProcess private val isMainProcess: Boolean,
    @ProcessName private val processName: String,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    private val autofillServiceFeature: AutofillServiceFeature,
) : AutofillServiceFeatureRepository, PrivacyConfigCallbackPlugin {

    override val exceptions = CopyOnWriteArrayList<String>()

    init {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            Timber.i("DDGAutofillService: Init AutofillFeatureRepository from $processName")
            loadToMemory()
        }
    }

    override fun onPrivacyConfigDownloaded() {
        loadToMemory()
    }

    private fun loadToMemory() {
        appCoroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess || processName == ":autofill") {
                exceptions.clear()
                exceptions.addAll(autofillServiceFeature.self().getExceptions().map { it.domain })
            }
        }
    }
}
