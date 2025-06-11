

package com.duckduckgo.installation.impl.installer

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.RequiresApi
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface InstallSourceExtractor {

    /**
     * Extracts the installer package name from the PackageManager.
     */
    fun extract(): String?
}

@ContributesBinding(AppScope::class)
class RealInstallSourceExtractor @Inject constructor(
    private val context: Context,
    private val appBuildConfig: AppBuildConfig,
) : InstallSourceExtractor {

    @SuppressLint("NewApi")
    override fun extract(): String? {
        return if (appBuildConfig.sdkInt >= 30) {
            installationSourceModern(context.packageName)
        } else {
            installationSourceLegacy(context.packageName)
        }
    }

    @Suppress("DEPRECATION")
    private fun installationSourceLegacy(packageName: String): String? {
        return context.packageManager.getInstallerPackageName(packageName)
    }

    @RequiresApi(30)
    private fun installationSourceModern(packageName: String): String? {
        return context.packageManager.getInstallSourceInfo(packageName).installingPackageName
    }
}
