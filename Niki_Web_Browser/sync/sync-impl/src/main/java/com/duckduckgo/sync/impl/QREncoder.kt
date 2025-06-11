

package com.duckduckgo.sync.impl

import android.content.Context
import android.graphics.*
import androidx.annotation.DimenRes
import com.google.zxing.BarcodeFormat.QR_CODE
import com.google.zxing.EncodeHintType
import com.journeyapps.barcodescanner.*
import javax.inject.*

interface QREncoder {
    fun encodeAsBitmap(
        textToEncode: String,
        width: Int,
        height: Int,
    ): Bitmap
}

class AppQREncoder constructor(
    private val context: Context,
    private val barcodeEncoder: BarcodeEncoder,
) : QREncoder {

    override fun encodeAsBitmap(textToEncode: String, @DimenRes width: Int, @DimenRes height: Int): Bitmap {
        return barcodeEncoder.encodeBitmap(
            textToEncode,
            QR_CODE,
            context.resources.getDimensionPixelSize(width),
            context.resources.getDimensionPixelSize(height),
            mapOf(EncodeHintType.MARGIN to 0),
        )
    }
}
