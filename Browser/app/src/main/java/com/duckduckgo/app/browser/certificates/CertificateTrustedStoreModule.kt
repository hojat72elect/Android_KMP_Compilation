

package com.duckduckgo.app.browser.certificates

import android.content.Context
import com.duckduckgo.app.browser.certificates.rootstore.IsrgRootX1
import com.duckduckgo.app.browser.certificates.rootstore.IsrgRootX2
import com.duckduckgo.app.browser.certificates.rootstore.LetsEncryptE1
import com.duckduckgo.app.browser.certificates.rootstore.LetsEncryptR3
import com.duckduckgo.app.browser.certificates.rootstore.TrustedCertificateStore
import com.duckduckgo.app.browser.certificates.rootstore.TrustedCertificateStoreImpl
import com.duckduckgo.di.scopes.AppScope
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

@Module
class CertificateTrustedStoreModule {
    @Provides
    @SingleInstanceIn(AppScope::class)
    fun trustedCertificateStore(
        letsEncryptCertificateProvider: LetsEncryptCertificateProvider,
    ): TrustedCertificateStore = TrustedCertificateStoreImpl(letsEncryptCertificateProvider)

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun letsEncryptCertificateProvider(
        context: Context,
    ): LetsEncryptCertificateProvider = LetsEncryptCertificateProviderImpl(
        setOf(
            IsrgRootX1(context),
            IsrgRootX2(context),
            LetsEncryptR3(context),
            LetsEncryptE1(context),
        ),
    )

    @Provides
    @SingleInstanceIn(AppScope::class)
    fun trustedSitesRepository(): BypassedSSLCertificatesRepository {
        return RealBypassedSSLCertificatesRepository()
    }
}
