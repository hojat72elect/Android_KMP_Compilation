

package com.duckduckgo.downloads.impl

import android.os.Bundle
import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.downloads.api.DownloadConfirmation
import com.duckduckgo.downloads.api.FileDownloader.PendingFileDownload
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class RealDownloadConfirmation @Inject constructor() : DownloadConfirmation {
    override fun instance(pendingDownload: PendingFileDownload): BottomSheetDialogFragment {
        val fragment = DownloadConfirmationFragment()
        fragment.isCancelable = false
        val bundle = Bundle()
        bundle.putSerializable(PENDING_DOWNLOAD_BUNDLE_KEY, pendingDownload)
        fragment.arguments = bundle
        return fragment
    }

    companion object {
        const val PENDING_DOWNLOAD_BUNDLE_KEY = "PENDING_DOWNLOAD_BUNDLE_KEY"
    }
}
