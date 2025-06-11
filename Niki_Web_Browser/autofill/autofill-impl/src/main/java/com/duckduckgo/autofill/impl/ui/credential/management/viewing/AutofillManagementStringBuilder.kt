

package com.duckduckgo.autofill.impl.ui.credential.management.viewing

import android.content.Context
import android.content.res.Resources
import com.duckduckgo.autofill.impl.R
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.FragmentScope
import com.duckduckgo.sync.api.DeviceSyncState
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext

interface AutofillManagementStringBuilder {
    fun stringForDeletePasswordDialogConfirmationTitle(numberToDelete: Int): String
    suspend fun stringForDeletePasswordDialogConfirmationMessage(numberToDelete: Int): String
}

@ContributesBinding(FragmentScope::class)
class AutofillManagementStringBuilderImpl @Inject constructor(
    private val context: Context,
    private val deviceSyncState: DeviceSyncState,
    private val dispatchers: DispatcherProvider,
) : AutofillManagementStringBuilder {

    override fun stringForDeletePasswordDialogConfirmationTitle(numberToDelete: Int): String {
        if (numberToDelete == 1) {
            return context.getString(R.string.credentialManagementDeleteAllPasswordsDialogConfirmationTitleSingular)
        }

        return context.resources.getQuantityString(
            R.plurals.credentialManagementDeleteAllPasswordsDialogConfirmationTitlePlural,
            numberToDelete,
            numberToDelete,
        )
    }

    override suspend fun stringForDeletePasswordDialogConfirmationMessage(numberToDelete: Int): String {
        val firstMessage = context.resources.deleteAllPasswordsWarning(numberToDelete)
        val secondMessage = if (numberToDelete == 1) {
            context.resources.getString(R.string.credentialManagementDeleteAllSecondInstructionSingular)
        } else {
            context.resources.getQuantityString(R.plurals.credentialManagementDeleteAllSecondInstructionPlural, numberToDelete)
        }
        return "$firstMessage $secondMessage"
    }

    private suspend fun Resources.deleteAllPasswordsWarning(numberToDelete: Int): String {
        return withContext(dispatchers.io()) {
            return@withContext if (deviceSyncState.isUserSignedInOnDevice()) {
                if (numberToDelete == 1) {
                    getString(R.string.credentialManagementDeleteAllPasswordsFirstInstructionSyncedSingular)
                } else {
                    getQuantityString(R.plurals.credentialManagementDeleteAllPasswordsFirstInstructionSyncedPlural, numberToDelete)
                }
            } else {
                if (numberToDelete == 1) {
                    getString(R.string.credentialManagementDeleteAllPasswordsDialogFirstInstructionNotSyncedSingular)
                } else {
                    getQuantityString(R.plurals.credentialManagementDeleteAllPasswordsDialogFirstInstructionNotSyncedPlural, numberToDelete)
                }
            }
        }
    }
}
