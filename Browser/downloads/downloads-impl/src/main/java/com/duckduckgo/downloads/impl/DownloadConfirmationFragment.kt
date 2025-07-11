

package com.duckduckgo.downloads.impl

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.duckduckgo.anvil.annotations.InjectWith
import com.duckduckgo.common.utils.baseHost
import com.duckduckgo.di.scopes.FragmentScope
import com.duckduckgo.downloads.api.DownloadConfirmationDialogListener
import com.duckduckgo.downloads.api.FileDownloader.PendingFileDownload
import com.duckduckgo.downloads.impl.RealDownloadConfirmation.Companion.PENDING_DOWNLOAD_BUNDLE_KEY
import com.duckduckgo.downloads.impl.databinding.DownloadConfirmationBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import java.io.File
import javax.inject.Inject
import logcat.logcat

@InjectWith(FragmentScope::class)
class DownloadConfirmationFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.DownloadsBottomSheetDialogTheme

    val listener: DownloadConfirmationDialogListener
        get() {
            return if (parentFragment != null) {
                parentFragment as DownloadConfirmationDialogListener
            } else {
                activity as DownloadConfirmationDialogListener
            }
        }

    @Inject
    lateinit var filenameExtractor: FilenameExtractor

    private var file: File? = null

    private val pendingDownload: PendingFileDownload by lazy {
        requireArguments()[PENDING_DOWNLOAD_BUNDLE_KEY] as PendingFileDownload
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = DownloadConfirmationBinding.inflate(inflater, container, false)
        setupDownload()
        setupViews(binding)
        return binding.root
    }

    private fun setupDownload() {
        file = if (!pendingDownload.isDataUrl) {
            when (val filenameExtraction = filenameExtractor.extract(pendingDownload)) {
                is FilenameExtractor.FilenameExtractionResult.Guess -> null
                is FilenameExtractor.FilenameExtractionResult.Extracted -> File(pendingDownload.directory, filenameExtraction.filename)
            }
        } else {
            null
        }
    }

    private fun setupViews(binding: DownloadConfirmationBinding) {
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        val fileName = file?.name ?: ""
        binding.downloadMessage.text = fileName
        binding.downloadMessageSubtitle.run {
            val host = runCatching { Uri.parse(pendingDownload.url).baseHost }.getOrNull()

            isVisible = !host.isNullOrBlank()
            text = getString(R.string.downloadConfirmationSubtitle, host)
        }
        binding.continueDownload.setOnClickListener {
            listener.continueDownload(pendingDownload)
            dismiss()
        }
        binding.cancel.setOnClickListener {
            logcat { "Cancelled download for url ${pendingDownload.url}" }
            listener.cancelDownload()
            dismiss()
        }
    }
}
