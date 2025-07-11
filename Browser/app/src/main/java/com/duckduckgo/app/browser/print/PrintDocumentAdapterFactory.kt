

package com.duckduckgo.app.browser.print

import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import timber.log.Timber

class PrintDocumentAdapterFactory {
    companion object {
        fun createPrintDocumentAdapter(
            printDocumentAdapter: PrintDocumentAdapter,
            onStartCallback: () -> Unit,
            onFinishCallback: () -> Unit,
        ): PrintDocumentAdapter {
            return object : PrintDocumentAdapter() {
                override fun onStart() {
                    printDocumentAdapter.onStart()
                    onStartCallback()
                }

                override fun onLayout(
                    oldAttributes: PrintAttributes?,
                    newAttributes: PrintAttributes?,
                    cancellationSignal: CancellationSignal?,
                    callback: LayoutResultCallback?,
                    extras: Bundle?,
                ) {
                    printDocumentAdapter.onLayout(oldAttributes, newAttributes, cancellationSignal, callback, extras)
                }

                override fun onWrite(
                    pages: Array<out PageRange>?,
                    destination: ParcelFileDescriptor?,
                    cancellationSignal: CancellationSignal?,
                    callback: WriteResultCallback?,
                ) {
                    runCatching {
                        printDocumentAdapter.onWrite(pages, destination, cancellationSignal, callback)
                    }.onFailure { exception ->
                        Timber.e(exception, "Failed to write document")
                        callback?.onWriteCancelled()
                    }
                }

                override fun onFinish() {
                    printDocumentAdapter.onFinish()
                    onFinishCallback()
                }
            }
        }
    }
}
