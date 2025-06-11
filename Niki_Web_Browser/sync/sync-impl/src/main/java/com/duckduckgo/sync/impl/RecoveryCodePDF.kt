

package com.duckduckgo.sync.impl

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo.Builder
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.WorkerThread
import com.duckduckgo.common.ui.view.toPx
import com.duckduckgo.common.utils.checkMainThread
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.sync.impl.databinding.ViewRecoveryCodeBinding
import com.squareup.anvil.annotations.ContributesBinding
import java.io.File
import java.io.FileOutputStream
import javax.inject.*

interface RecoveryCodePDF {
    @WorkerThread
    fun generateAndStoreRecoveryCodePDF(
        viewContext: Context,
        recoveryCodeB64: String,
    ): File
}

@ContributesBinding(ActivityScope::class)
class RecoveryCodePDFImpl @Inject constructor(
    private val qrEncoder: QREncoder,
) : RecoveryCodePDF {

    override fun generateAndStoreRecoveryCodePDF(
        viewContext: Context,
        recoveryCodeB64: String,
    ): File {
        checkMainThread()

        val bitmapQR = qrEncoder.encodeAsBitmap(recoveryCodeB64, R.dimen.qrSizeLarge, R.dimen.qrSizeLarge)
        val pdfDocument = PdfDocument()
        val inflater = LayoutInflater.from(viewContext)
        val page = pdfDocument.startPage(Builder(a4PageWidth.toPx(), a4PageHeight.toPx(), 1).create())
        ViewRecoveryCodeBinding.inflate(inflater, null, false).apply {
            this.qrCodeImageView.setImageBitmap(bitmapQR)
            this.recoveryCodeText.text = recoveryCodeB64
            val measureWidth: Int = View.MeasureSpec.makeMeasureSpec(page.canvas.width, View.MeasureSpec.EXACTLY)
            val measuredHeight: Int = View.MeasureSpec.makeMeasureSpec(page.canvas.height, View.MeasureSpec.EXACTLY)
            this.root.measure(measureWidth, measuredHeight)
            this.root.layout(0, 0, page.canvas.width, page.canvas.height)
            this.root.draw(page.canvas)
        }
        pdfDocument.finishPage(page)

        val syncDirectory = File(viewContext.cacheDir, PDF_CACHE_FOLDER)
        if (!syncDirectory.exists()) {
            syncDirectory.mkdir()
        }
        val file = File(syncDirectory, PDF_FILE_NAME)

        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        return file
    }

    companion object {
        private const val PDF_FILE_NAME = "Sync Data Recovery - DuckDuckGo.pdf"
        private const val PDF_CACHE_FOLDER = "sync"
        private const val a4PageWidth = 612
        private const val a4PageHeight = 792
    }
}
