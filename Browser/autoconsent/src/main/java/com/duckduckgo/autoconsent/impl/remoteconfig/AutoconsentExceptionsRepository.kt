package com.duckduckgo.autoconsent.impl.remoteconfig

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.di.IsMainProcess
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

interface AutoconsentExceptionsRepository {
    val exceptions: CopyOnWriteArrayList<FeatureException>
}

@SingleInstanceIn(AppScope::class)
@ContributesBinding(
    scope = AppScope::class,
    boundType = AutoconsentExceptionsRepository::class,
)
@ContributesMultibinding(
    scope = AppScope::class,
    boundType = PrivacyConfigCallbackPlugin::class,
)
class RealAutoconsentExceptionsRepository @Inject constructor(
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    private val autoconsentFeature: AutoconsentFeature,
    @IsMainProcess private val isMainProcess: Boolean,
) : AutoconsentExceptionsRepository, PrivacyConfigCallbackPlugin {

    override val exceptions = CopyOnWriteArrayList<FeatureException>()

    init {
        loadToMemory()
    }

    private fun loadToMemory() {
        coroutineScope.launch(dispatcherProvider.io()) {
            if (isMainProcess) {
                exceptions.clear()
                exceptions.addAll(autoconsentFeature.self().getExceptions())
            }
        }
    }

    override fun onPrivacyConfigDownloaded() {
        loadToMemory()
    }
}
