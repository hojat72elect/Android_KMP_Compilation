

package com.duckduckgo.referral

import com.duckduckgo.app.di.AppCoroutineScope
import com.duckduckgo.app.pixels.AppPixelName
import com.duckduckgo.app.referral.AppReferrerDataStore
import com.duckduckgo.app.statistics.api.AtbLifecyclePlugin
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesMultibinding(scope = AppScope::class)
class AppReferrerInstallPixelSender @Inject constructor(
    private val appReferrerDataStore: AppReferrerDataStore,
    private val pixel: Pixel,
    @AppCoroutineScope private val appCoroutineScope: CoroutineScope,
    private val dispatchers: DispatcherProvider,
    private val appBuildConfig: AppBuildConfig,
) : AtbLifecyclePlugin {

    private val pixelSent = AtomicBoolean(false)

    override fun onAppAtbInitialized() {
        Timber.v("AppReferrerInstallPixelSender: onAppAtbInitialized")
        sendPixelIfUnsent()
    }

    private fun sendPixelIfUnsent() {
        if (pixelSent.compareAndSet(false, true)) {
            appCoroutineScope.launch(dispatchers.io()) {
                sendOriginAttribute(appReferrerDataStore.utmOriginAttributeCampaign)
            }
        }
    }

    private suspend fun sendOriginAttribute(originAttribute: String?) {
        val returningUser = appBuildConfig.isAppReinstall()

        val params = mutableMapOf(
            PIXEL_PARAM_LOCALE to appBuildConfig.deviceLocale.toLanguageTag(),
            PIXEL_PARAM_RETURNING_USER to returningUser.toString(),
        )

        // if origin is null, pixel is sent with origin omitted
        if (originAttribute != null) {
            params[PIXEL_PARAM_ORIGIN] = originAttribute
        }

        pixel.fire(pixel = AppPixelName.REFERRAL_INSTALL_UTM_CAMPAIGN, type = Pixel.PixelType.Unique(), parameters = params)
    }

    companion object {
        const val PIXEL_PARAM_ORIGIN = "origin"
        const val PIXEL_PARAM_LOCALE = "locale"
        const val PIXEL_PARAM_RETURNING_USER = "reinstall"
    }
}
