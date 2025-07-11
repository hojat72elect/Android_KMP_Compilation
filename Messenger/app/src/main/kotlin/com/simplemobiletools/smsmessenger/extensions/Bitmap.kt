package com.simplemobiletools.smsmessenger.extensions

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.CompressFormat.extension() = when (this) {
    Bitmap.CompressFormat.PNG -> "png"
    Bitmap.CompressFormat.WEBP -> "webp"
    else -> "jpg"
}


fun Bitmap.getByteArray(): ByteArray {
    var baos: ByteArrayOutputStream? = null
    try {
        baos = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, baos)
        return baos.toByteArray()
    } finally {
        baos?.close()
    }
}
