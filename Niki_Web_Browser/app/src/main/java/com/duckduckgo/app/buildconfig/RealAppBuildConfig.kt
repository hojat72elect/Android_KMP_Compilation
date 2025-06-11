package com.duckduckgo.app.buildconfig

import android.os.Build
import android.os.Environment
import androidx.core.content.edit
import com.duckduckgo.app.browser.BuildConfig
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.appbuildconfig.api.BuildFlavor
import com.duckduckgo.appbuildconfig.api.isInternalBuild
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.data.store.api.SharedPreferencesProvider
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.experiments.api.VariantManager
import com.squareup.anvil.annotations.ContributesBinding
import dagger.Lazy
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.Locale
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealAppBuildConfig @Inject constructor(
    private val variantManager: Lazy<VariantManager>, // break any possible DI dependency cycle
    private val dispatcherProvider: DispatcherProvider,
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) : AppBuildConfig {
    private val preferences by lazy {
        sharedPreferencesProvider.getSharedPreferences("com.duckduckgo.app.buildconfig.cache", false, false)
    }

    override val isDebug: Boolean = BuildConfig.DEBUG
    override val applicationId: String = BuildConfig.APPLICATION_ID
    override val buildType: String = BuildConfig.BUILD_TYPE
    override val versionCode: Int = BuildConfig.VERSION_CODE
    override val versionName: String = BuildConfig.VERSION_NAME
    override val flavor: BuildFlavor
        get() = when (BuildConfig.FLAVOR) {
            "internal" -> BuildFlavor.INTERNAL
            "play" -> BuildFlavor.PLAY
            else -> throw IllegalStateException("Unknown app flavor")
        }
    override val sdkInt: Int = Build.VERSION.SDK_INT
    override val manufacturer: String = Build.MANUFACTURER
    override val model: String = Build.MODEL
    override val isTest by lazy {
        try {
            Class.forName("org.junit.Test")
            true
        } catch (e: Exception) {
            false
        }
    }
    override val isPerformanceTest: Boolean = BuildConfig.IS_PERFORMANCE_TEST

    override val isDefaultVariantForced: Boolean = BuildConfig.FORCE_DEFAULT_VARIANT

    override val deviceLocale: Locale
        get() = Locale.getDefault()

    override val variantName: String?
        get() = variantManager.get().getVariantKey()

    override suspend fun isAppReinstall(): Boolean = withContext(dispatcherProvider.io()) {
        return@withContext kotlin.runCatching {
            if (sdkInt < 30) {
                return@withContext false
            }

            if (preferences.contains(APP_REINSTALLED_KEY)) {
                return@withContext preferences.getBoolean(APP_REINSTALLED_KEY, false)
            }

            val downloadDirectory = getDownloadsDirectory()
            val ddgDirectoryExists = (downloadDirectory.list()?.asList() ?: emptyList()).contains(DDG_DOWNLOADS_DIRECTORY)
            val appReinstallValue = if (!ddgDirectoryExists) {
                createNewDirectory(DDG_DOWNLOADS_DIRECTORY)
                // this is a new install
                false
            } else {
                true
            }
            preferences.edit(commit = true) { putBoolean(APP_REINSTALLED_KEY, appReinstallValue) }
            return@withContext appReinstallValue
        }.getOrDefault(false)
    }

    override val buildDateTimeMillis: Long
        get() = BuildConfig.BUILD_DATE_MILLIS

    override val canSkipOnboarding: Boolean
        get() = BuildConfig.CAN_SKIP_ONBOARDING || isInternalBuild()

    private fun getDownloadsDirectory(): File {
        val downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadDirectory.exists()) {
            Timber.i("Download directory doesn't exist; trying to create it. %s", downloadDirectory.absolutePath)
            downloadDirectory.mkdirs()
        }
        return downloadDirectory
    }

    private fun createNewDirectory(directoryName: String) {
        val directory = File(getDownloadsDirectory(), directoryName)
        val success = directory.mkdirs()
        Timber.i("Directory creation success: %s", success)
        if (!success) {
            Timber.e("Directory creation failed")
            kotlin.runCatching {
                val directoryCreationSuccess = directory.createNewFile()
                Timber.i("File creation success: %s", directoryCreationSuccess)
            }.onFailure {
                Timber.w("Failed to create file: %s", it.message)
            }
        }
    }

    companion object {
        private const val APP_REINSTALLED_KEY = "appReinstalled"
        private const val DDG_DOWNLOADS_DIRECTORY = "DuckDuckGo"
    }
}
