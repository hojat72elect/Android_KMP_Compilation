

package com.duckduckgo.sync.impl

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.duckduckgo.appbuildconfig.api.AppBuildConfig
import java.io.File
import javax.inject.*
import timber.log.Timber

class ShareAction @Inject constructor(private val appBuildConfig: AppBuildConfig) {

    fun shareFile(applicationContext: Context, file: File): Boolean {
        val intent = createShareIntent(applicationContext, file)
        return if (intent != null) startActivity(applicationContext, intent) else false
    }

    private fun createShareIntent(applicationContext: Context, file: File): Intent? {
        val fileUri = getFilePathUri(applicationContext, file)
        val intent =
            Intent().apply {
                setDataAndType(fileUri, applicationContext.contentResolver?.getType(fileUri))
                action = Intent.ACTION_SEND
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                putExtra(Intent.EXTRA_STREAM, fileUri)
            }
        return Intent.createChooser(
            intent,
            applicationContext.getString(R.string.sync_share_title),
        )
            .apply {
                if (appBuildConfig.sdkInt >= Build.VERSION_CODES.Q) {
                    // Show a thumbnail preview of the file to be shared on Android Q and above.
                    clipData = ClipData.newRawUri(fileUri.toString(), fileUri)
                }
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
    }

    private fun getFilePathUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, "${appBuildConfig.applicationId}.provider", file)
    }

    private fun startActivity(applicationContext: Context, intent: Intent): Boolean {
        return try {
            applicationContext.startActivity(intent)
            true
        } catch (error: ActivityNotFoundException) {
            Timber.e("No suitable activity found to satisfy intent $intent")
            false
        }
    }
}
