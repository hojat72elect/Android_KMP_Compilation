package com.amaze.fileutilities.docx_viewer

import android.content.Intent
import androidx.lifecycle.ViewModel

class DocxViewerActivityViewModel : ViewModel() {
    var nightMode = false
    private var docxModel: LocalDocxModel? = null

    fun getDocxModel(intent: Intent?): LocalDocxModel? {
        if (docxModel == null) {
            intent?.let {
                val mimeType = intent.type
                val docxUri = intent.data ?: return null
                docxModel = LocalDocxModel(docxUri, mimeType)
            }
        }
        return docxModel
    }
}
