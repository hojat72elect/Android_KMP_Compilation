

package com.duckduckgo.app.browser.certificates

import android.net.http.SslCertificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

/**
 * Converts the [SslCertificate] type to [X509Certificate]
 *
 * @throws CertificateException when the [X509Certificate] can't be generated
 */
fun SslCertificate.toX509Certificate(): X509Certificate {
    val bundle = SslCertificate.saveState(this)
    val bytes = bundle.getByteArray("x509-certificate")
    return CertificateFactory.getInstance(CertificateTypes.X509)
        .generateCertificate(bytes?.inputStream()) as X509Certificate
}
