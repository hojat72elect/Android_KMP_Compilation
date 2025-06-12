package com.amaze.fileutilities.home_page.ui.files

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.format.DateUtils
import androidx.core.content.FileProvider
import com.amaze.fileutilities.R
import com.amaze.fileutilities.audio_player.AudioPlayerDialogActivity
import com.amaze.fileutilities.home_page.ui.options.CastActivity
import com.amaze.fileutilities.image_viewer.ImageViewerDialogActivity
import com.amaze.fileutilities.utilis.FileUtils
import com.amaze.fileutilities.utilis.Utils
import com.amaze.fileutilities.utilis.showToastOnBottom
import com.amaze.fileutilities.video_player.VideoPlayerDialogActivity
import com.amaze.trashbin.TrashBinConfig
import com.amaze.trashbin.TrashBinFile
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import java.io.File
import java.lang.ref.WeakReference
import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class MediaFileInfo(
    val title: String,
    val path: String,
    val date: Long = 0,
    val longSize: Long = 0,
    var isDirectory: Boolean = false,
    var listHeader: String = "",
    var extraInfo: ExtraInfo? = null,
    var contentUri: Uri? = null,
    val id: Long = -1,
) {

    companion object {
        var log: Logger = LoggerFactory.getLogger(MediaFileInfo::class.java)

        //        private const val DATE_TIME_FORMAT = "%s %s, %s"
        private const val UNKNOWN = "UNKNOWN"
        const val MEDIA_TYPE_UNKNOWN = 4
        const val MEDIA_TYPE_IMAGE = 3
        const val MEDIA_TYPE_AUDIO = 0
        const val MEDIA_TYPE_VIDEO = 1
        const val MEDIA_TYPE_DOCUMENT = 2
        const val MEDIA_TYPE_APK = 5
        const val MEDIA_TYPE_TRASH_BIN = 6

        fun fromFile(file: File, extraInfo: ExtraInfo): MediaFileInfo {
            return MediaFileInfo(
                file.name, file.path, file.lastModified(), file.length(),
                extraInfo = extraInfo
            )
        }

        fun fromFile(
            mediaType: Int,
            id: Long,
            name: String,
            path: String,
            modified: Long,
            size: Long,
            context: Context,
            extraInfo: ExtraInfo
        ): MediaFileInfo {
            return MediaFileInfo(
                name, path, modified, size,
                extraInfo = extraInfo,
                contentUri = if (mediaType == MEDIA_TYPE_AUDIO) {
                    // we want to load uri only for audio files for finding current playing item
                    FileProvider.getUriForFile(context, context.packageName, File(path))
                } else {
                    null
                },
                id = id
            )
        }

        fun fromTrashBinFile(
            trashBinFile: TrashBinFile,
            trashBinConfig: TrashBinConfig
        ): MediaFileInfo {
            return MediaFileInfo(
                trashBinFile.fileName,
                trashBinFile.getDeletedPath(trashBinConfig),
                trashBinFile.deleteTime ?: 0L,
                trashBinFile.sizeBytes,
                extraInfo = ExtraInfo(
                    MEDIA_TYPE_TRASH_BIN,
                    null, null, null, null,
                    TrashBinData(trashBinFile.path)
                )
            )
        }

        fun fromApplicationInfo(
            context: Context,
            applicationInfo: ApplicationInfo,
            sizeDiff: Long = -1,
            timeForeground: Long = 0L
        ): MediaFileInfo? {
            if (applicationInfo.sourceDir == null) {
                return null
            }
            try {
                val apkFile = File(applicationInfo.sourceDir)
                val packageManager = context.packageManager

                val mediaFileInfo = MediaFileInfo(
                    applicationInfo.loadLabel(packageManager) as String,
                    applicationInfo.sourceDir,
                    apkFile.lastModified(),
                    Utils.findApplicationInfoSize(context, applicationInfo),
                    false
                )
                val extraInfo = ExtraInfo(
                    MEDIA_TYPE_APK,
                    null, null, null,
                    ApkMetaData(
                        applicationInfo.packageName,
                        packageManager.getApplicationIcon(applicationInfo.packageName),
                        Utils.getApplicationNetworkBytes(context, applicationInfo),
                        sizeDiff, timeForeground
                    )
                )
                mediaFileInfo.extraInfo = extraInfo
                return mediaFileInfo
            } catch (e: Exception) {
                log.warn("failed to form mediafileinfo from application info", e)
                return null
            }
        }
    }

    fun getGlideRequest(context: Context): RequestBuilder<Drawable> {
        if (this.extraInfo == null) {
            return Glide.with(context).load(this.path)
        } else {
            return when (this.extraInfo!!.mediaType) {
                MEDIA_TYPE_AUDIO -> {
                    val toLoadBitmap: Bitmap? = this.extraInfo?.audioMetaData?.albumArt
                    Glide.with(context).load(toLoadBitmap)
                }

                MEDIA_TYPE_APK -> {
                    val drawable = this.extraInfo?.apkMetaData?.drawable
                    Glide.with(context).load(drawable)
                }

                else -> {
                    Glide.with(context).load(this.path)
                }
            }
        }
    }

    fun getModificationDate(context: Context): String {
        return DateUtils.formatDateTime(context, date, DateUtils.FORMAT_ABBREV_ALL)
    }

    fun getParentName(): String {
        return if (exists()) {
            File(path).parentFile?.name ?: UNKNOWN
        } else {
            UNKNOWN
        }
    }

    fun getParentPath(): String {
        return if (exists()) {
            File(path).parentFile?.path ?: UNKNOWN
        } else {
            UNKNOWN
        }
    }

    fun getFormattedSize(context: Context): String {
        return FileUtils.formatStorageLength(context, longSize)
    }

    fun toTrashBinFile(): TrashBinFile {
        return TrashBinFile(
            title, false,
            extraInfo?.trashBinData?.originalFilePath ?: path,
            longSize
        )
    }

    fun delete(): Boolean {
        if (!exists()) {
            return true
        }
        return File(path).delete()
    }

    fun getContentUri(context: Context): Uri? {
        return if (contentUri == null && exists()) {
            val uri = FileUtils.getContentUri(context, path)
            contentUri = uri
            uri
        } else {
            contentUri
        }
    }

    fun triggerMediaFileInfoAction(contextRef: WeakReference<Context>) {
        if (!exists()) {
            contextRef.get()?.let { context ->
                context.showToastOnBottom(context.resources.getString(R.string.file_not_found))
                return
            }
        }
        contextRef.get()?.let { context ->
            val castActivity = (context as CastActivity)
            when (this.extraInfo?.mediaType) {
                MEDIA_TYPE_IMAGE -> {
                    castActivity.showCastFileDialog(
                        this,
                        MediaFileAdapter.MEDIA_TYPE_IMAGES
                    ) {
                        startImageViewer(this, context)
                    }
                }

                MEDIA_TYPE_VIDEO -> {
                    castActivity.showCastFileDialog(
                        this,
                        MediaFileAdapter.MEDIA_TYPE_VIDEO
                    ) {
                        startVideoViewer(this, context)
                    }
                }

                MEDIA_TYPE_AUDIO -> {
                    castActivity.showCastFileDialog(
                        this,
                        MediaFileAdapter.MEDIA_TYPE_AUDIO
                    ) {
                        startAudioViewer(this, context)
                    }
                }

                MEDIA_TYPE_DOCUMENT, MEDIA_TYPE_UNKNOWN, MEDIA_TYPE_TRASH_BIN -> {
                    castActivity.showCastFileDialog(
                        this,
                        MediaFileAdapter.MEDIA_TYPE_DOCS
                    ) {
                        startExternalViewAction(this, context)
                    }
                }

                MEDIA_TYPE_APK -> {
                    extraInfo?.apkMetaData?.packageName?.let { packageName ->
                        if (!Utils.openExternalAppInfoScreen(context, packageName)) {
                            context.showToastOnBottom(
                                context.resources
                                    .getString(R.string.operation_failed)
                            )
                        }
                    }
                }

                else -> {
                    startExternalViewAction(this, context)
                }
            }
        }
    }

    fun exists(): Boolean {
        val file = File(this.path)
        return file.exists()
    }

    fun startLocateFileAction(context: Context) {
        File(this.path).let { file ->
            if (file.parentFile != null) {
                val intent = Intent()
                intent.setDataAndType(
                    FileProvider.getUriForFile(context, context.packageName, file.parentFile!!),
                    "resource/folder"
                )
                intent.putExtra("com.amaze.fileutilities.AFM_LOCATE_FILE_NAME", file.name)
                intent.action = Intent.ACTION_VIEW
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    context.showToastOnBottom(context.resources.getString(R.string.no_app_found))
                }
            } else {
                context.showToastOnBottom(context.resources.getString(R.string.operation_failed))
            }
        }
    }

    private fun startExternalViewAction(mediaFileInfo: MediaFileInfo, context: Context) {
        val intent = Intent()
        intent.data = mediaFileInfo.getContentUri(context)
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            context.showToastOnBottom(context.resources.getString(R.string.no_app_found))
        }
    }

    private fun startImageViewer(mediaFileInfo: MediaFileInfo, context: Context) {
        val intent = Intent(context, ImageViewerDialogActivity::class.java)
        intent.data = mediaFileInfo.getContentUri(context)
        context.startActivity(intent)
    }

    private fun startAudioViewer(mediaFileInfo: MediaFileInfo, context: Context) {
        val intent = Intent(context, AudioPlayerDialogActivity::class.java)
        intent.data = mediaFileInfo.getContentUri(context)
        context.startActivity(intent)
    }

    private fun startVideoViewer(mediaFileInfo: MediaFileInfo, context: Context) {
        val intent = Intent(context, VideoPlayerDialogActivity::class.java)
        intent.data = mediaFileInfo.getContentUri(context)
        context.startActivity(intent)
    }

    data class ExtraInfo(
        var mediaType: Int,
        val audioMetaData: AudioMetaData?,
        val videoMetaData: VideoMetaData?,
        val imageMetaData: ImageMetaData?,
        val apkMetaData: ApkMetaData? = null,
        val trashBinData: TrashBinData? = null,
        val extraMetaData: ExtraMetaData? = null
    )

    data class AudioMetaData(
        val albumName: String?,
        val artistName: String?,
        val duration: Long?,
        val albumId: Long?,
        var albumArt: Bitmap?,
        var idInPlaylist: Long?,
        var playlist: Playlist?,
    )

    data class VideoMetaData(val duration: Long?, val width: Int?, val height: Int?)
    data class ImageMetaData(val width: Int?, val height: Int?)
    data class ApkMetaData(
        val packageName: String,
        val drawable: Drawable?,
        val networkBytes: Long,
        val sizeDiff: Long = -1,
        val timeForeground: Long = 0
    )

    data class ExtraMetaData(val checksum: String)
    data class Playlist(var id: Long, var name: String)
    data class TrashBinData(var originalFilePath: String)
}
