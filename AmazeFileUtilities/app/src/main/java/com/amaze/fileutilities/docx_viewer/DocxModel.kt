package com.amaze.fileutilities.docx_viewer

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import com.amaze.fileutilities.utilis.BaseIntentModel
import com.amaze.fileutilities.utilis.getDocumentFileFromUri
import java.io.InputStream
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalDocxModel(
    private var uri: Uri,
    val mimeType: String?
) : Parcelable, DocxModel {
    override fun getInputStream(context: Context): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }

    override fun getUri(): Uri {
        return uri
    }

    override fun getName(context: Context): String {
        uri.getDocumentFileFromUri(context)?.name?.run {
            return this
        }
        uri.path?.run {
            return this
        }
        return uri.toString()
    }
}

interface DocxModel : BaseIntentModel {
    fun getInputStream(context: Context): InputStream?
}
