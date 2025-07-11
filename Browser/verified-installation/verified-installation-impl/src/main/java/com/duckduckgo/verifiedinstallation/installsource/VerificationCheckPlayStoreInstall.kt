

package com.duckduckgo.verifiedinstallation.installsource

import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface VerificationCheckPlayStoreInstall {
    fun installedFromPlayStore(): Boolean
}

@ContributesBinding(AppScope::class)
class VerificationCheckPlayStoreInstallImpl @Inject constructor(
    private val installSourceExtractor: InstallSourceExtractor,
) : VerificationCheckPlayStoreInstall {

    override fun installedFromPlayStore(): Boolean {
        val installationSource = installSourceExtractor.extract()
        return installationSource == PLAY_STORE_PACKAGE_NAME
    }

    companion object {
        private const val PLAY_STORE_PACKAGE_NAME = "com.android.vending"
    }
}
