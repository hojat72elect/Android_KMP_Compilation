

package com.duckduckgo.app.browser.certificates

import android.net.http.SslCertificate
import java.security.cert.X509Certificate

interface LetsEncryptCertificateProvider {
    fun certificates(): List<LetsEncryptCertificate>

    fun findByCname(cname: String): LetsEncryptCertificate?
}

class LetsEncryptCertificateProviderImpl constructor(
    private val certificates: Set<LetsEncryptCertificate>,
) : LetsEncryptCertificateProvider {
    override fun certificates(): List<LetsEncryptCertificate> {
        return certificates.toList()
    }

    override fun findByCname(cname: String): LetsEncryptCertificate? {
        return certificates
            .asSequence()
            .filter { SslCertificate(it.certificate() as X509Certificate).issuedTo.cName == cname }
            .firstOrNull()
    }
}
