package com.amaze.fileutilities.utilis

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.amaze.fileutilities.R
import com.amaze.fileutilities.home_page.ui.files.FilesViewModel
import com.amaze.fileutilities.home_page.ui.files.MediaFileInfo
import com.amaze.fileutilities.utilis.Utils.Companion.showProcessingDialog

abstract class AbstractMediaFileInfoOperationsFragment : Fragment() {

    abstract fun getFilesViewModelObj(): FilesViewModel
    abstract fun uninstallAppCallback(mediaFileInfo: MediaFileInfo)

    private val uninstallAppFilter = IntentFilter().apply {
        addAction(Intent.ACTION_PACKAGE_REMOVED)
        addDataScheme("package")
    }

    private val uninstallAppReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val actionStr = intent.action
            if (Intent.ACTION_PACKAGE_REMOVED == actionStr) {
                val data: Uri? = intent.data
                val pkgName = data?.encodedSchemeSpecificPart ?: ""
                // dummy object as we're just refreshing the whole list instead of removing an element from apps list
                uninstallAppCallback(
                    MediaFileInfo(
                        pkgName, pkgName, 0, 0,
                        false, "",
                        MediaFileInfo.ExtraInfo(
                            MediaFileInfo.MEDIA_TYPE_APK, null,
                            null, null, null
                        )
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(uninstallAppReceiver, uninstallAppFilter)
    }

    override fun onPause() {
        requireActivity().unregisterReceiver(uninstallAppReceiver)
        super.onPause()
    }

    /**
     * setup delete permanently click
     * @param toDelete to delete media files
     * @param deletedCallback callback once deletion finishes
     */
    fun setupDeletePermanentlyButton(
        toDelete: List<MediaFileInfo>,
        deletedCallback: () -> Unit
    ) {
        if (toDelete.isEmpty()) {
            requireContext().showToastOnBottom(getString(R.string.no_item_selected))
            return
        }
        val progressDialogBuilder = requireContext()
            .showProcessingDialog(layoutInflater, "")
        val summaryDialogBuilder = Utils.buildDeletePermanentlySummaryDialog(requireContext()) {
            if (toDelete[0].extraInfo?.mediaType == MediaFileInfo.MEDIA_TYPE_APK) {
                toDelete.forEachIndexed { _, mediaFileInfo ->
                    Utils.uninstallPackage(
                        mediaFileInfo.extraInfo!!.apkMetaData!!.packageName,
                        requireActivity()
                    )
                }
            } else {
                val progressDialog = progressDialogBuilder.create()
                progressDialog.show()
                getFilesViewModelObj().deleteMediaFiles(toDelete).observe(viewLifecycleOwner) {
                    progressDialog.findViewById<TextView>(R.id.please_wait_text)?.text =
                        resources.getString(R.string.deleted_progress)
                            .format(it.first, toDelete.size)
                    if (it.second == toDelete.size) {
                        deletedCallback.invoke()
                        progressDialog.dismiss()
                    }
                }
            }
        }
        val summaryDialog = summaryDialogBuilder.create()
        summaryDialog.show()
        getFilesViewModelObj().getMediaFileListSize(toDelete)
            .observe(viewLifecycleOwner) { sizeRaw ->
                if (summaryDialog.isShowing) {
                    val size = Formatter.formatFileSize(requireContext(), sizeRaw)
                    summaryDialog.setMessage(
                        resources.getString(R.string.delete_files_message)
                            .format(toDelete.size, size)
                    )
                }
            }
    }

    /**
     * setup compress click
     * @param toCompress to delete media files
     * @param compressedCallback callback once compression finishes
     */
    fun setupCompressImagesButton(
        toCompress: List<MediaFileInfo>,
        layoutInflater: LayoutInflater,
        compressedCallback: (toDelete: List<MediaFileInfo>, toAdd: List<MediaFileInfo>) -> Unit
    ) {
        if (toCompress.isEmpty()) {
            requireContext().showToastOnBottom(getString(R.string.no_item_selected))
            return
        }
        val progressDialogBuilder = requireContext()
            .showProcessingDialog(layoutInflater, "")
        val summaryDialogBuilder = Utils.buildCompressImagesSummaryDialog(
            requireContext(),
            layoutInflater
        ) { compressQuality, qualityFormat, deletePermanently ->
            val progressDialog = progressDialogBuilder.create()
            progressDialog.show()
            val compressedFiles = mutableListOf<MediaFileInfo>()
            getFilesViewModelObj().compressMediaFiles(
                toCompress,
                compressQuality, qualityFormat, deletePermanently
            )
                .observe(viewLifecycleOwner) {
                    progressDialog.findViewById<TextView>(R.id.please_wait_text)?.text =
                        resources.getString(R.string.processed_compressed)
                            .format(it.first, Formatter.formatFileSize(requireContext(), it.second))
                    if (it.third != null) {
                        compressedFiles.add(it.third!!)
                    }
                    if (it.first == toCompress.size) {
                        if (deletePermanently) {
                            compressedCallback.invoke(toCompress, compressedFiles)
                        } else {
                            compressedCallback.invoke(emptyList(), compressedFiles)
                        }
                        progressDialog.setCancelable(true)
                        progressDialog
                            .findViewById<ProgressBar>(R.id.loadingProgress)?.visibility = View.GONE
                    }
                }
        }
        val summaryDialog = summaryDialogBuilder.create()
        summaryDialog.show()
        getFilesViewModelObj().getMediaFileListSize(toCompress)
            .observe(viewLifecycleOwner) { sizeRaw ->
                if (summaryDialog.isShowing) {
                    val size = Formatter.formatFileSize(requireContext(), sizeRaw)
                    summaryDialog.findViewById<TextView>(R.id.compression_message)?.text =
                        resources.getString(R.string.compress_message)
                            .format(toCompress.size, size)
                }
            }
    }

    /**
     * setup compress click
     * @param toCompress to delete media files
     * @param compressedCallback callback once compression finishes
     */
    @SuppressLint("SetTextI18n")
    fun setupCompressVideosButton(
        toCompress: List<MediaFileInfo>,
        layoutInflater: LayoutInflater,
        compressedCallback: (toDelete: List<MediaFileInfo>, toAdd: List<MediaFileInfo>) -> Unit
    ) {
        if (toCompress.isEmpty()) {
            requireContext().showToastOnBottom(getString(R.string.no_item_selected))
            return
        }
        val progressDialogBuilder = requireContext()
            .showProcessingDialog(layoutInflater, "")
        val summaryDialogBuilder = Utils.buildCompressVideosSummaryDialog(
            requireContext(),
            layoutInflater
        ) { compressQuality, disableAudio, deletePermanently ->
            val progressDialog = progressDialogBuilder.create()
            progressDialog.show()
            progressDialog
                .findViewById<ProgressBar>(R.id.loadingProgress)?.visibility = View.GONE
            progressDialog
                .findViewById<TextView>(R.id.loadingProgressText)?.visibility = View.VISIBLE
            progressDialog.findViewById<TextView>(R.id.please_wait_text)?.text =
                resources.getString(R.string.processed_compressed)
                    .format("0", "0B")
            val compressedFiles = mutableListOf<MediaFileInfo>()
            getFilesViewModelObj().compressMediaFiles(
                toCompress,
                compressQuality, disableAudio, deletePermanently, { progressMap ->
                    requireActivity().runOnUiThread {
                        val progressText = StringBuffer()
                        progressMap.forEach {
                            progressText.append("${it.key}: ${it.value}%\n")
                        }
                        progressDialog
                            .findViewById<TextView>(R.id.loadingProgressText)?.text = progressText
                            .toString()
                    }
                }
            ) {
                progressDialog.findViewById<TextView>(R.id.please_wait_text)?.text =
                    resources.getString(R.string.processed_compressed)
                        .format(it.first, Formatter.formatFileSize(requireContext(), it.second))
                if (it.third != null) {
                    compressedFiles.add(it.third!!)
                }
                if (it.first == toCompress.size) {
                    if (deletePermanently) {
                        compressedCallback.invoke(toCompress, compressedFiles)
                    } else {
                        compressedCallback.invoke(emptyList(), compressedFiles)
                    }
                    progressDialog.setCancelable(true)
                    progressDialog
                        .findViewById<ProgressBar>(R.id.loadingProgress)?.visibility = View.GONE
                    progressDialog
                        .findViewById<TextView>(R.id.loadingProgressText)?.visibility = View.GONE
                }
            }
        }
        val summaryDialog = summaryDialogBuilder.create()
        summaryDialog.show()
        getFilesViewModelObj().getMediaFileListSize(toCompress)
            .observe(viewLifecycleOwner) { sizeRaw ->
                if (summaryDialog.isShowing) {
                    val size = Formatter.formatFileSize(requireContext(), sizeRaw)
                    summaryDialog.findViewById<TextView>(R.id.compression_message)?.text =
                        resources.getString(R.string.compress_message)
                            .format(toCompress.size, size)
                }
            }
    }

    /**
     * setup delete click
     * @param toDelete to delete media files
     * @param deletedCallback callback once deletion finishes
     */
    fun setupDeleteButton(
        toDelete: List<MediaFileInfo>,
        deletedCallback: () -> Unit
    ) {
        if (toDelete.isEmpty()) {
            requireContext().showToastOnBottom(getString(R.string.no_item_selected))
            return
        }
        val progressDialogBuilder = requireContext()
            .showProcessingDialog(layoutInflater, "")
        val summaryDialogBuilder =
            Utils.buildDeleteSummaryDialog(requireContext()) { deletePermanently ->
                val progressDialog = progressDialogBuilder.create()
                progressDialog.show()
                if (deletePermanently) {
                    getFilesViewModelObj().deleteMediaFiles(toDelete).observe(viewLifecycleOwner) {
                        progressDialog.findViewById<TextView>(R.id.please_wait_text)?.text =
                            resources.getString(R.string.deleted_progress)
                                .format(it.first, toDelete.size)
                        if (it.second == toDelete.size) {
                            deletedCallback.invoke()
                            progressDialog.dismiss()
                        }
                    }
                } else {
                    getFilesViewModelObj().moveToTrashBin(toDelete).observe(viewLifecycleOwner) {
                        progressDialog.findViewById<TextView>(R.id.please_wait_text)?.text =
                            resources.getString(R.string.deleted_progress)
                                .format(it.first, toDelete.size)
                        if (it.second == toDelete.size) {
                            deletedCallback.invoke()
                            progressDialog.dismiss()
                        }
                    }
                }
            }
        val summaryDialog = summaryDialogBuilder.create()
        summaryDialog.show()
        getFilesViewModelObj().getMediaFileListSize(toDelete)
            .observe(viewLifecycleOwner) { sizeRaw ->
                if (summaryDialog.isShowing) {
                    val size = Formatter.formatFileSize(requireContext(), sizeRaw)
                    summaryDialog.findViewById<TextView>(R.id.dialog_summary)?.text =
                        resources.getString(R.string.delete_files_temporarily_message)
                            .format(toDelete.size, size)
                }
            }
    }

    /**
     * setup restore click
     * @param toRestore to restore media files
     * @param deletedCallback callback once deletion finishes
     */
    fun setupRestoreButton(
        toRestore: List<MediaFileInfo>,
        deletedCallback: () -> Unit
    ) {
        if (toRestore.isEmpty()) {
            requireContext().showToastOnBottom(getString(R.string.no_item_selected))
            return
        }
        val progressDialogBuilder = requireContext()
            .showProcessingDialog(layoutInflater, "")
        val summaryDialogBuilder = Utils.buildRestoreSummaryDialog(requireContext()) {
            val progressDialog = progressDialogBuilder.create()
            progressDialog.show()
            getFilesViewModelObj().restoreFromBin(toRestore).observe(viewLifecycleOwner) {
                progressDialog.findViewById<TextView>(R.id.please_wait_text)?.text =
                    resources.getString(R.string.restored_progress)
                        .format(it.first, toRestore.size)
                if (it.second == toRestore.size) {
                    deletedCallback.invoke()
                    progressDialog.dismiss()
                }
            }
        }
        val summaryDialog = summaryDialogBuilder.create()
        summaryDialog.show()
        getFilesViewModelObj().getMediaFileListSize(toRestore)
            .observe(viewLifecycleOwner) { sizeRaw ->
                if (summaryDialog.isShowing) {
                    val size = Formatter.formatFileSize(requireContext(), sizeRaw)
                    summaryDialog.setMessage(
                        resources.getString(R.string.trash_bin_restore_dialog_message)
                            .format(toRestore.size, size)
                    )
                }
            }
    }
}
