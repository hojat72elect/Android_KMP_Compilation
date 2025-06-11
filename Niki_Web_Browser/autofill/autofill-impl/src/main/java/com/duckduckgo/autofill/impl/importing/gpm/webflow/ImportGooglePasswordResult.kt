

package com.duckduckgo.autofill.impl.importing.gpm.webflow

import android.os.Parcelable
import com.duckduckgo.autofill.impl.importing.gpm.webflow.ImportGooglePasswordsWebFlowViewModel.UserCannotImportReason
import kotlinx.parcelize.Parcelize

sealed interface ImportGooglePasswordResult : Parcelable {

    @Parcelize
    data object Success : ImportGooglePasswordResult

    @Parcelize
    data class UserCancelled(val stage: String) : ImportGooglePasswordResult

    @Parcelize
    data class Error(val reason: UserCannotImportReason) : ImportGooglePasswordResult

    companion object {
        const val RESULT_KEY = "importResult"
        const val RESULT_KEY_DETAILS = "importResultDetails"
    }
}
