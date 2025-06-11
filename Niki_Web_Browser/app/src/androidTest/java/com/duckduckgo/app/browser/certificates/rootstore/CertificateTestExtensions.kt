

package com.duckduckgo.app.browser.certificates.rootstore

import android.net.http.SslCertificate
import com.duckduckgo.app.browser.certificates.CertificateTypes.Companion.X509
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import okio.ByteString
import okio.ByteString.Companion.decodeBase64

fun String.parsePemCertificate(): ByteString? {
    return this
        .replace("-----BEGIN CERTIFICATE-----", "")
        .replace("-----END CERTIFICATE-----", "")
        .decodeBase64()
}

fun ByteString.toX509Certificate(): X509Certificate {
    val certificateFactory = CertificateFactory.getInstance(X509)
    return certificateFactory.generateCertificate(this.toByteArray().inputStream()) as X509Certificate
}

fun ByteString.toSslCertificate(): SslCertificate {
    return SslCertificate(this.toX509Certificate())
}
