

package com.duckduckgo.verifiedinstallation.buildtype

import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.appbuildconfig.api.BuildFlavor.PLAY
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface VerificationCheckBuildType {
    fun isPlayReleaseBuild(): Boolean
}

@ContributesBinding(AppScope::class)
class VerificationCheckBuildTypeImpl @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
) : VerificationCheckBuildType {

    override fun isPlayReleaseBuild(): Boolean {
        return appBuildConfig.flavor == PLAY && !appBuildConfig.isDebug
    }
}
