

package com.duckduckgo.savedsites.impl.folders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.common.utils.SingleLiveEvent
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.savedsites.api.SavedSitesRepository
import com.duckduckgo.savedsites.api.models.BookmarkFolder
import com.duckduckgo.savedsites.api.models.BookmarkFolderItem
import com.duckduckgo.savedsites.impl.dialogs.AddBookmarkFolderDialogFragment.AddBookmarkFolderListener
import com.duckduckgo.savedsites.impl.folders.BookmarkFoldersViewModel.Command.NewFolderCreatedUpdateTheStructure
import com.duckduckgo.savedsites.impl.folders.BookmarkFoldersViewModel.Command.SelectFolder
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesViewModel(ActivityScope::class)
class BookmarkFoldersViewModel @Inject constructor(
    val savedSitesRepository: SavedSitesRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel(), AddBookmarkFolderListener {

    data class ViewState(
        val folderStructure: List<BookmarkFolderItem> = emptyList(),
    )

    sealed class Command {
        class SelectFolder(val selectedBookmarkFolder: BookmarkFolder) : Command()
        object NewFolderCreatedUpdateTheStructure : Command()
    }

    val viewState: MutableLiveData<ViewState> = MutableLiveData()
    val command: SingleLiveEvent<Command> = SingleLiveEvent()

    init {
        viewState.value = ViewState()
    }

    fun fetchBookmarkFolders(
        selectedFolderId: String,
        currentFolder: BookmarkFolder?,
    ) {
        Timber.d("Saved sites: selectedFolderId $selectedFolderId")
        Timber.d("Saved sites: currentFolder $currentFolder")
        viewModelScope.launch(dispatcherProvider.io()) {
            val folderStructure = savedSitesRepository.getFolderTree(selectedFolderId, currentFolder)
            onFolderStructureCreated(folderStructure)
        }
    }

    fun newFolderAdded(
        selectedFolderId: String,
        currentFolder: BookmarkFolder?,
    ) {
        viewModelScope.launch(dispatcherProvider.io()) {
            val folderStructure = savedSitesRepository.getFolderTree(
                selectedFolderId,
                currentFolder,
            )
            onFolderStructureCreated(folderStructure)
        }
    }

    private fun onFolderStructureCreated(folderStructure: List<BookmarkFolderItem>) {
        viewState.postValue(viewState.value?.copy(folderStructure = folderStructure))
    }

    fun onItemSelected(bookmarkFolder: BookmarkFolder) {
        command.value = SelectFolder(bookmarkFolder)
    }

    override fun onBookmarkFolderAdded(bookmarkFolder: BookmarkFolder) {
        viewModelScope.launch(dispatcherProvider.io()) {
            savedSitesRepository.insert(bookmarkFolder)
        }
        command.value = NewFolderCreatedUpdateTheStructure
    }
}
