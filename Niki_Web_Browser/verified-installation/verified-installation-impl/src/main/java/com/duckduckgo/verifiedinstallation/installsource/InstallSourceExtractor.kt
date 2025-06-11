

package com.duckduckgo.verifiedinstallation.installsource

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface InstallSourceExtractor {
    fun extract(): String?
}

@ContributesBinding(AppScope::class)
class InstallSourceExtractorImpl @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
    private val context: Context,
) : InstallSourceExtractor {

    @SuppressLint("NewApi")
    override fun extract(): String? {
        return kotlin.runCatching {
            if (appBuildConfig.sdkInt >= VERSION_CODES.R) {
                installationSourceModern(context.packageName)
            } else {
                installationSourceLegacy(context.packageName)
            }
        }.getOrNull()
    }

    @Suppress("DEPRECATION")
    private fun installationSourceLegacy(packageName: String): String? {
        return context.packageManager.getInstallerPackageName(packageName)
    }

    @RequiresApi(VERSION_CODES.R)
    private fun installationSourceModern(packageName: String): String? {
        return context.packageManager.getInstallSourceInfo(packageName).installingPackageName
    }
}
