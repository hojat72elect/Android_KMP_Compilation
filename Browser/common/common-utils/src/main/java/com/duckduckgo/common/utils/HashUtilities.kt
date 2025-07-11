

package com.duckduckgo.common.utils

import java.math.BigInteger
import java.security.MessageDigest

val ByteArray.sha256: String
    get() = sha("SHA-256", this)

fun ByteArray.verifySha256(sha256: String): Boolean {
    return this.sha256 == sha256
}

val String.sha256: String
    get() = sha("SHA-256", this.toByteArray())

val String.sha1: String
    get() = sha("SHA-1", this.toByteArray())

private fun sha(
    algorithm: String,
    bytes: ByteArray,
): String {
    val md = MessageDigest.getInstance(algorithm)
    val digest = md.digest(bytes)
    return String.format("%0" + digest.size * 2 + "x", BigInteger(1, digest))
}

fun String.verifySha1(sha1: String): Boolean {
    return this.sha1 == sha1
}
