

package com.duckduckgo.autofill.impl.service.mapper

import android.content.Context
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface AppFingerprintProvider {
    fun getSHA256HexadecimalFingerprint(packageName: String): List<String>
}

@ContributesBinding(AppScope::class)
class RealAppFingerprintProvider @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
    private val context: Context,
) : AppFingerprintProvider {
    override fun getSHA256HexadecimalFingerprint(packageName: String): List<String> =
        context.packageManager.getSHA256HexadecimalFingerprintCompat(packageName, appBuildConfig)
}
