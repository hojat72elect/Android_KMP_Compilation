package ca.hojat.gamehub.core.downloader

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import ca.hojat.gamehub.R


/**
 * Notifies us of when the download has finished (even if the app isn't
 * running when the download completes).
 */
class DownloadCompletionReceiver : BroadcastReceiver() {

    /**
     * Gets called for any of the broadcasts that we receive.
     */
    override fun onReceive(context: Context?, intent: Intent?) {

        if (context != null && intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (id != -1L) {
                Toast.makeText(
                    context,
                    context.getString(R.string.image_viewer_download_completion_prompt),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}