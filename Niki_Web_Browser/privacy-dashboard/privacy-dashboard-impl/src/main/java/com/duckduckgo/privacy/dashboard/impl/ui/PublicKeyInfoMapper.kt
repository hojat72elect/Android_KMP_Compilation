

package com.duckduckgo.privacy.dashboard.impl.ui

import android.annotation.SuppressLint
import android.net.http.SslCertificate
import android.os.Build.VERSION_CODES
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import java.security.cert.X509Certificate
import java.security.interfaces.DSAPublicKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPublicKey
import javax.inject.Inject

class PublicKeyInfoMapper @Inject constructor(
    private val appBuildConfig: AppBuildConfig,
) {

    fun mapFrom(sslCertificate: SslCertificate): PublicKeyInfo? {
        return sslCertificate.publicKeyInfo()
    }

    @SuppressLint("NewApi")
    private fun SslCertificate.publicKeyInfo(): PublicKeyInfo? {
        if (appBuildConfig.sdkInt < VERSION_CODES.Q) return null

        return this.x509Certificate?.let { it ->
            val bitSize = certificateBitSize(it)
            PublicKeyInfo(
                type = it.publicKey.algorithm,
                bitSize = bitSize,
            )
        }
    }

    private fun SslCertificate.certificateBitSize(it: X509Certificate) = when (val publicKey = it.publicKey) {
        is RSAPublicKey -> {
            publicKey.modulus.bitLength()
        }
        is DSAPublicKey -> {
            runCatching {
                publicKey.params?.let {
                    it.p.bitLength()
                } ?: publicKey.y.bitLength()
            }.getOrNull()
        }
        is ECPublicKey -> {
            runCatching {
                publicKey.params.order.bitLength()
            }.getOrNull()
        }
        else -> null
    }

    data class PublicKeyInfo(
        val blockSize: Int? = null,
        val canEncrypt: Boolean? = null,
        val bitSize: Int? = null,
        val canSign: Boolean? = null,
        val canDerive: Boolean? = null,
        val canUnwrap: Boolean? = null,
        val canWrap: Boolean? = null,
        val canDecrypt: Boolean? = null,
        val effectiveSize: Int? = null,
        val isPermanent: Boolean? = null,
        val type: String? = null,
        val externalRepresentation: String? = null,
        val canVerify: Boolean? = null,
        val keyId: String? = null,
    )
}
