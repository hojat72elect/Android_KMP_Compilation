

package com.duckduckgo.privacy.config.impl

import androidx.annotation.WorkerThread
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.common.utils.extensions.extractETag
import com.duckduckgo.common.utils.plugins.PluginPoint
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.config.api.PrivacyConfigCallbackPlugin
import com.duckduckgo.privacy.config.impl.PrivacyConfigDownloader.ConfigDownloadResult.Error
import com.duckduckgo.privacy.config.impl.PrivacyConfigDownloader.ConfigDownloadResult.Success
import com.duckduckgo.privacy.config.impl.RealPrivacyConfigDownloader.DownloadError.DOWNLOAD_ERROR
import com.duckduckgo.privacy.config.impl.RealPrivacyConfigDownloader.DownloadError.EMPTY_CONFIG_ERROR
import com.duckduckgo.privacy.config.impl.RealPrivacyConfigDownloader.DownloadError.STORE_ERROR
import com.duckduckgo.privacy.config.impl.network.PrivacyConfigService
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import timber.log.Timber

/** Public interface for download remote privacy config */
interface PrivacyConfigDownloader {
    /**
     * This method will download remote config and returns the [ConfigDownloadResult]
     * @return [ConfigDownloadResult.Success] if remote config has been downloaded correctly or
     * [ConfigDownloadResult.Error] otherwise.
     */
    suspend fun download(): ConfigDownloadResult

    sealed class ConfigDownloadResult {
        data object Success : ConfigDownloadResult()
        data class Error(val error: String?) : ConfigDownloadResult()
    }
}

@WorkerThread
@ContributesBinding(AppScope::class)
class RealPrivacyConfigDownloader @Inject constructor(
    private val privacyConfigService: PrivacyConfigService,
    private val privacyConfigPersister: PrivacyConfigPersister,
    private val privacyConfigCallbacks: PluginPoint<PrivacyConfigCallbackPlugin>,
    private val pixel: Pixel,
) : PrivacyConfigDownloader {

    override suspend fun download(): PrivacyConfigDownloader.ConfigDownloadResult {
        Timber.d("Downloading privacy config")

        val response = runCatching {
            privacyConfigService.privacyConfig()
        }.onSuccess { response ->
            val eTag = response.headers().extractETag()
            response.body()?.let {
                runCatching {
                    privacyConfigPersister.persistPrivacyConfig(it, eTag)
                    privacyConfigCallbacks.getPlugins().forEach { callback -> callback.onPrivacyConfigDownloaded() }
                }.onFailure {
                    // error parsing remote config
                    notifyErrorToCallbacks(STORE_ERROR)
                    return Error(it.localizedMessage)
                }
            } ?: run {
                // empty response
                notifyErrorToCallbacks(EMPTY_CONFIG_ERROR)
                return Error(null)
            }
        }.onFailure {
            // error downloading remote config
            Timber.w(it.localizedMessage)
            notifyErrorToCallbacks(DOWNLOAD_ERROR)
        }

        return if (response.isFailure) {
            // error downloading remote config
            Error(response.exceptionOrNull()?.localizedMessage)
        } else {
            Success
        }
    }

    private fun notifyErrorToCallbacks(reason: DownloadError) {
        pixel.fire(reason.pixelName)
    }

    private enum class DownloadError(val pixelName: String) {
        DOWNLOAD_ERROR("m_privacy_config_download_error"),
        STORE_ERROR("m_privacy_config_store_error"),
        EMPTY_CONFIG_ERROR("m_privacy_config_empty_error"),
    }
}
