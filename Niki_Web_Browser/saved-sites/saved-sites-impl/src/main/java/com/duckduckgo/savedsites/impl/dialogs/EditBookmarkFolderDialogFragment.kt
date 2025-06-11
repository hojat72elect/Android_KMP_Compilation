

package com.duckduckgo.savedsites.impl.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.text.toSpannable
import com.duckduckgo.saved.sites.impl.R
import com.duckduckgo.saved.sites.impl.R.plurals
import com.duckduckgo.savedsites.api.models.BookmarkFolder
import com.duckduckgo.savedsites.impl.folders.BookmarkFoldersActivity

class EditBookmarkFolderDialogFragment : SavedSiteDialogFragment() {

    interface EditBookmarkFolderListener {
        fun onBookmarkFolderUpdated(bookmarkFolder: BookmarkFolder)

        fun onDeleteBookmarkFolderRequestedFromEdit(bookmarkFolder: BookmarkFolder)
    }

    var listener: EditBookmarkFolderListener? = null

    override fun configureUI() {
        setToolbarTitle(getString(R.string.editFolder))
        showAddFolderMenu = true
        getBookmarkFolder()?.let {
            binding.titleInput.text = it.name
        }
        configureFieldVisibility()
    }

    private fun configureFieldVisibility() {
        binding.savedSiteLocationContainer.visibility = View.VISIBLE
        binding.urlInput.visibility = View.GONE
        val toolbar = binding.savedSiteAppBar.toolbar
        toolbar.menu.findItem(R.id.action_delete).isVisible = true
    }

    private fun getBookmarkFolder(): BookmarkFolder? =
        requireArguments().getSerializable(BookmarkFoldersActivity.KEY_CURRENT_FOLDER) as BookmarkFolder?

    override fun onConfirmation() {
        arguments?.getString(KEY_PARENT_FOLDER_ID)?.let {
            val name = binding.titleInput.text
            if (name.isNotBlank()) {
                val bookmarkFolder = arguments?.getSerializable(BookmarkFoldersActivity.KEY_CURRENT_FOLDER) as BookmarkFolder
                listener?.onBookmarkFolderUpdated(bookmarkFolder.copy(name = name, parentId = it))
            }
        }
    }

    override fun deleteConfirmationTitle() = getString(R.string.deleteFolder, getBookmarkFolder()?.name)

    override fun deleteConfirmationMessage() = getBookmarkFolder()?.let { folder ->
        getMessageString(bookmarkFolder = folder)
    }?.toSpannable()

    private fun getMessageString(bookmarkFolder: BookmarkFolder): String {
        val totalItems = bookmarkFolder.numBookmarks + bookmarkFolder.numFolders
        return resources.getQuantityString(
            plurals.bookmarkFolderDeleteMessage,
            totalItems,
            totalItems,
        )
    }

    override fun onDeleteConfirmed() {
        getBookmarkFolder()?.let {
            listener?.onDeleteBookmarkFolderRequestedFromEdit(it)
        }
        dismiss()
    }

    override fun onDeleteCancelled() {
        dismiss()
    }

    override fun onDeleteRequested() {
        // no-op
    }

    companion object {
        const val KEY_PARENT_FOLDER_ID = "KEY_PARENT_FOLDER_ID"
        const val KEY_PARENT_FOLDER_NAME = "KEY_PARENT_FOLDER_NAME"

        fun instance(
            parentFolderId: String,
            parentFolderName: String,
            bookmarkFolder: BookmarkFolder,
        ): EditBookmarkFolderDialogFragment {
            val dialogFragment = EditBookmarkFolderDialogFragment()
            val bundle = Bundle()
            bundle.putString(KEY_PARENT_FOLDER_ID, parentFolderId)
            bundle.putString(KEY_PARENT_FOLDER_NAME, parentFolderName)
            bundle.putSerializable(BookmarkFoldersActivity.KEY_CURRENT_FOLDER, bookmarkFolder)
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }
}
