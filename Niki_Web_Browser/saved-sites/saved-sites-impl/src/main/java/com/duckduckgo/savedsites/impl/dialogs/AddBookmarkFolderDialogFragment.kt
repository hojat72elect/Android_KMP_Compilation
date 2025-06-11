

package com.duckduckgo.savedsites.impl.dialogs

import android.os.Bundle
import android.view.View
import com.duckduckgo.saved.sites.impl.R
import com.duckduckgo.savedsites.api.models.BookmarkFolder
import java.util.*

class AddBookmarkFolderDialogFragment : SavedSiteDialogFragment() {

    interface AddBookmarkFolderListener {
        fun onBookmarkFolderAdded(bookmarkFolder: BookmarkFolder)
    }

    var listener: AddBookmarkFolderListener? = null

    override fun configureUI() {
        setToolbarTitle(getString(R.string.addFolder))
        configureFieldVisibility()
    }

    private fun configureFieldVisibility() {
        binding.savedSiteLocationContainer.visibility = View.VISIBLE
        binding.urlInput.visibility = View.GONE
    }

    override fun onConfirmation() {
        arguments?.getString(KEY_PARENT_FOLDER_ID)?.let {
            val name = binding.titleInput.text
            if (name.isNotBlank()) {
                listener?.onBookmarkFolderAdded(BookmarkFolder(name = name, parentId = it))
            }
        }
    }

    companion object {
        const val KEY_PARENT_FOLDER_ID = "KEY_PARENT_FOLDER_ID"
        const val KEY_PARENT_FOLDER_NAME = "KEY_PARENT_FOLDER_NAME"

        fun instance(
            parentFolderId: String,
            parentFolderName: String,
        ): AddBookmarkFolderDialogFragment {
            val dialogFragment = AddBookmarkFolderDialogFragment()
            val bundle = Bundle()
            bundle.putString(KEY_PARENT_FOLDER_ID, parentFolderId)
            bundle.putString(KEY_PARENT_FOLDER_NAME, parentFolderName)
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }
}
