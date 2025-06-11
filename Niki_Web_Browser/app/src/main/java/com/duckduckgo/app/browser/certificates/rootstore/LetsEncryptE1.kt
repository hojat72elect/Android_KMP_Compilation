

package com.duckduckgo.app.browser.certificates.rootstore

import android.content.Context
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.certificates.CertificateType
import com.duckduckgo.app.browser.certificates.CertificateTypes
import com.duckduckgo.app.browser.certificates.LetsEncryptCertificate
import java.security.cert.Certificate
import java.security.cert.CertificateFactory

/**
 * Let's Encrypt E1 intermediate certificate.
 * It is signed by [IsrgRootX2]
 */
class LetsEncryptE1(
    private val context: Context,
) : LetsEncryptCertificate {

    private val certificate: Certificate by lazy {
        val certificateFactory = CertificateFactory.getInstance(CertificateTypes.X509)
        val certificate = certificateFactory.generateCertificate(context.resources.openRawResource(R.raw.lets_encrypt_e1))
        certificate
    }

    override fun certificate(): Certificate {
        return certificate
    }

    override fun type(): CertificateType {
        return CertificateType.Intermediate
    }
}
