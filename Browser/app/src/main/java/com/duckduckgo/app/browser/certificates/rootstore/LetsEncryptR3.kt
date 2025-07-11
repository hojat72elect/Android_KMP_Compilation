

package com.duckduckgo.app.browser.certificates.rootstore

import android.content.Context
import com.duckduckgo.app.browser.R
import com.duckduckgo.app.browser.certificates.CertificateType
import com.duckduckgo.app.browser.certificates.CertificateTypes
import com.duckduckgo.app.browser.certificates.LetsEncryptCertificate
import java.security.cert.Certificate
import java.security.cert.CertificateFactory

/**
 * Let's Encrypt R3 intermediate certificate.
 * It is signed by [IsrgRootX1]
 */
class LetsEncryptR3(
    private val context: Context,
) : LetsEncryptCertificate {

    private val certificate: Certificate by lazy {
        val certificateFactory = CertificateFactory.getInstance(CertificateTypes.X509)
        val certificate = certificateFactory.generateCertificate(context.resources.openRawResource(R.raw.lets_encrypt_r3))
        certificate
    }

    override fun certificate(): Certificate {
        return certificate
    }

    override fun type(): CertificateType {
        return CertificateType.Intermediate
    }
}
