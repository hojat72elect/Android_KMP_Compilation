

package com.duckduckgo.autofill.impl.feature.plugin

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
import com.duckduckgo.autofill.api.AutofillFeature
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.feature.toggles.api.FeatureException
import com.duckduckgo.privacy.config.api.PrivacyConfigCallbackPlugin
import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.ContributesMultibinding
import dagger.SingleInstanceIn
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface AutofillFeatureRepository {
    val exceptions: CopyOnWriteArrayList<FeatureException>
}

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = PrivacyConfigCallbackPlugin::class,
)
@ContributesBinding(
    scope = AppScope::class,
    boundType = AutofillFeatureRepository::class,
)
@SingleInstanceIn(AppScope::class)
class RealAutofillFeatureRepository @Inject constructor(
    private val feature: AutofillFeature,
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    @IsMainProcess private val isMainProcess: Boolean,
) : AutofillFeatureRepository, PrivacyConfigCallbackPlugin {

    override val exceptions = CopyOnWriteArrayList<FeatureException>()

    init {
        loadToMemory()
    }

    override fun onPrivacyConfigDownloaded() {
        loadToMemory()
    }

    private fun loadToMemory() {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                exceptions.clear()
                exceptions.addAll(feature.self().getExceptions())
            }
        }
    }
}
