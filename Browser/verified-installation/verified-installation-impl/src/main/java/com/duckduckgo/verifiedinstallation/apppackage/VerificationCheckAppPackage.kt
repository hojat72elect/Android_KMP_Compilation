

package com.duckduckgo.verifiedinstallation.apppackage

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface VerificationCheckAppPackage {
    fun isProductionPackage(): Boolean
}

@ContributesBinding(AppScope::class)
class VerificationCheckAppPackageImpl @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
) : VerificationCheckAppPackage {
    override fun isProductionPackage(): Boolean {
        return appBuildConfig.applicationId == "com.duckduckgo.mobile.android"
    }
}
