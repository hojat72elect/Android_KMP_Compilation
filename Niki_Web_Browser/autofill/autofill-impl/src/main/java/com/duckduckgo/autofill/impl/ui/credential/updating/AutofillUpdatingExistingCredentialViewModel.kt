

package com.duckduckgo.autofill.impl.ui.credential.updating

import androidx.lifecycle.ViewModel
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.di.scopes.FragmentScope
import javax.inject.Inject

@ContributesViewModel(FragmentScope::class)
class AutofillUpdatingExistingCredentialViewModel @Inject constructor() : ViewModel() {

    fun ellipsizeIfNecessary(username: String): String {
        return if (username.length > USERNAME_MAX_LENGTH) {
            username.take(USERNAME_MAX_LENGTH - 1) + Typography.ellipsis
        } else {
            username
        }
    }

    companion object {
        private const val USERNAME_MAX_LENGTH = 50
    }
}
