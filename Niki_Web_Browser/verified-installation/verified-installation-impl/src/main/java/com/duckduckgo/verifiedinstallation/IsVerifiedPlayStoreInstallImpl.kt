

package com.duckduckgo.verifiedinstallation

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.verifiedinstallation.apppackage.VerificationCheckAppPackage
import com.duckduckgo.verifiedinstallation.buildtype.VerificationCheckBuildType
import com.duckduckgo.verifiedinstallation.certificate.VerificationCheckBuildCertificate
import com.duckduckgo.verifiedinstallation.installsource.VerificationCheckPlayStoreInstall
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesBinding(AppScope::class)
@SingleInstanceIn(AppScope::class)
class IsVerifiedPlayStoreInstallImpl @Inject constructor(
    private val packageChecker: VerificationCheckAppPackage,
    private val buildTypeChecker: VerificationCheckBuildType,
    private val certificateChecker: VerificationCheckBuildCertificate,
    private val playStoreInstallChecker: VerificationCheckPlayStoreInstall,
) : IsVerifiedPlayStoreInstall {

    // cached in memory since none of the checks can change while the app is running
    private val isVerifiedInstall: Boolean by lazy {
        buildTypeChecker.isPlayReleaseBuild() &&
            packageChecker.isProductionPackage() &&
            playStoreInstallChecker.installedFromPlayStore() &&
            certificateChecker.builtWithVerifiedCertificate()
    }

    override fun invoke(): Boolean {
        return isVerifiedInstall
    }
}
