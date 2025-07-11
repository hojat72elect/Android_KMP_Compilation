

package com.duckduckgo.autofill.impl.service

import android.annotation.SuppressLint
import android.view.autofill.AutofillManager
import androidx.lifecycle.LifecycleOwner
import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.lifecycle.MainProcessLifecycleObserver
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.autofill.impl.pixel.AutofillPixelNames.AUTOFILL_SERVICE_DISABLED
import com.duckduckgo.autofill.impl.pixel.AutofillPixelNames.AUTOFILL_SERVICE_ENABLED
import com.duckduckgo.autofill.impl.service.store.AutofillServiceStore
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesMultibinding(
    scope = AppScope::class,
    boundType = MainProcessLifecycleObserver::class,
)
class AutofillServiceStatusLifecycleObserver@Inject constructor(
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    private val autofillManager: AutofillManager?,
    private val autofillServiceStore: AutofillServiceStore,
    private val appBuildConfig: AppBuildConfig,
    private val pixel: Pixel,
) : MainProcessLifecycleObserver {

    @SuppressLint("NewApi")
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        appCoroutineScope.launch(dispatcherProvider.io()) {
            Timber.e("Autofill service component observer status")
            runCatching {
                autofillManager ?: return@runCatching
                val isDefault = autofillManager.hasEnabledAutofillServices()
                var ddgIsServiceComponentName = false
                if (appBuildConfig.sdkInt >= 28) { // This is a fallback logic in case previous check fails
                    ddgIsServiceComponentName = autofillManager.autofillServiceComponentName
                        ?.packageName?.contains("com.duckduckgo.mobile.android") ?: false
                }
                val newDefaultState = isDefault || ddgIsServiceComponentName

                if (autofillServiceStore.isDefaultAutofillProvider() != newDefaultState) {
                    triggerPixel(newDefaultState)
                    autofillServiceStore.updateDefaultAutofillProvider(newDefaultState)
                }
            }.onFailure {
                Timber.e("Failed to update autofill service status")
            }
        }
    }

    private fun triggerPixel(newIsEnabledState: Boolean) {
        val pixelName = if (newIsEnabledState) AUTOFILL_SERVICE_ENABLED else AUTOFILL_SERVICE_DISABLED
        pixel.fire(pixelName)
    }
}
