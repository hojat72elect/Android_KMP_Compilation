

package com.duckduckgo.app.browser.certificates

import java.security.cert.Certificate

interface LetsEncryptCertificate {
    fun certificate(): Certificate

    fun type(): CertificateType
}

sealed class CertificateType {
    object Root : CertificateType()
    object Intermediate : CertificateType()
}
