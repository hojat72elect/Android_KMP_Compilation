

package com.duckduckgo.autofill.impl.ui.credential.management.importpassword.google

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.autofill.impl.importing.CredentialImporter
import com.duckduckgo.autofill.impl.importing.CredentialImporter.ImportResult
import com.duckduckgo.autofill.impl.importing.gpm.webflow.ImportGooglePasswordsWebFlowViewModel.UserCannotImportReason
import com.duckduckgo.autofill.impl.ui.credential.management.importpassword.ImportPasswordsPixelSender
import com.duckduckgo.autofill.impl.ui.credential.management.importpassword.google.ImportFromGooglePasswordsDialogViewModel.ViewMode.Importing
import com.duckduckgo.autofill.impl.ui.credential.management.importpassword.google.ImportFromGooglePasswordsDialogViewModel.ViewMode.PreImport
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.FragmentScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@ContributesViewModel(FragmentScope::class)
class ImportFromGooglePasswordsDialogViewModel @Inject constructor(
    private val credentialImporter: CredentialImporter,
    private val dispatchers: DispatcherProvider,
    private val importPasswordsPixelSender: ImportPasswordsPixelSender,
) : ViewModel() {

    fun onImportFlowFinishedSuccessfully() {
        viewModelScope.launch(dispatchers.main()) {
            observeImportJob()
        }
    }

    private suspend fun observeImportJob() {
        credentialImporter.getImportStatus().collect {
            when (it) {
                is ImportResult.InProgress -> {
                    Timber.d("Import in progress")
                    _viewState.value = ViewState(viewMode = Importing)
                }

                is ImportResult.Finished -> {
                    Timber.d("Import finished: ${it.savedCredentials} imported. ${it.numberSkipped} skipped.")
                    fireImportSuccessPixel(savedCredentials = it.savedCredentials, numberSkipped = it.numberSkipped)
                    _viewState.value = ViewState(viewMode = ViewMode.ImportSuccess(it))
                }
            }
        }
    }

    fun onImportFlowFinishedWithError(reason: UserCannotImportReason) {
        fireImportFailedPixel(reason)
        _viewState.value = ViewState(viewMode = ViewMode.ImportError)
    }

    fun onImportFlowCancelledByUser(stage: String) {
        importPasswordsPixelSender.onUserCancelledImportWebFlow(stage)
    }

    private fun fireImportSuccessPixel(savedCredentials: Int, numberSkipped: Int) {
        importPasswordsPixelSender.onImportSuccessful(savedCredentials = savedCredentials, numberSkipped = numberSkipped)
    }

    private fun fireImportFailedPixel(reason: UserCannotImportReason) {
        importPasswordsPixelSender.onImportFailed(reason)
    }

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState

    data class ViewState(val viewMode: ViewMode = PreImport)

    sealed interface ViewMode {
        data object PreImport : ViewMode
        data object Importing : ViewMode
        data class ImportSuccess(val importResult: ImportResult.Finished) : ViewMode
        data object ImportError : ViewMode
    }
}
