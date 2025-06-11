

package com.duckduckgo.autofill.impl.email.incontext.prompt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.anvil.annotations.ContributesViewModel
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.autofill.api.EmailProtectionInContextSignUpDialog.EmailProtectionInContextSignUpResult
import com.duckduckgo.autofill.api.EmailProtectionInContextSignUpDialog.EmailProtectionInContextSignUpResult.Cancel
import com.duckduckgo.autofill.api.EmailProtectionInContextSignUpDialog.EmailProtectionInContextSignUpResult.DoNotShowAgain
import com.duckduckgo.autofill.api.EmailProtectionInContextSignUpDialog.EmailProtectionInContextSignUpResult.SignUp
import com.duckduckgo.autofill.impl.email.incontext.prompt.EmailProtectionInContextSignUpPromptViewModel.Command.FinishWithResult
import com.duckduckgo.autofill.impl.pixel.AutofillPixelNames.EMAIL_PROTECTION_IN_CONTEXT_PROMPT_CONFIRMED
import com.duckduckgo.autofill.impl.pixel.AutofillPixelNames.EMAIL_PROTECTION_IN_CONTEXT_PROMPT_DISMISSED
import com.duckduckgo.autofill.impl.pixel.AutofillPixelNames.EMAIL_PROTECTION_IN_CONTEXT_PROMPT_NEVER_AGAIN
import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.FragmentScope
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@ContributesViewModel(FragmentScope::class)
class EmailProtectionInContextSignUpPromptViewModel @Inject constructor(
    val dispatchers: DispatcherProvider,
    val pixel: Pixel,
) : ViewModel() {

    private val _commands = Channel<Command>(1, DROP_OLDEST)
    val commands: Flow<Command> = _commands.receiveAsFlow()

    sealed interface Command {
        data class FinishWithResult(val result: EmailProtectionInContextSignUpResult) : Command
    }

    fun onProtectEmailButtonPressed() {
        pixel.fire(EMAIL_PROTECTION_IN_CONTEXT_PROMPT_CONFIRMED)
        FinishWithResult(SignUp).send()
    }

    fun onCloseButtonPressed() {
        pixel.fire(EMAIL_PROTECTION_IN_CONTEXT_PROMPT_DISMISSED)
        FinishWithResult(Cancel).send()
    }

    fun onDoNotShowAgainButtonPressed() {
        pixel.fire(EMAIL_PROTECTION_IN_CONTEXT_PROMPT_NEVER_AGAIN)
        FinishWithResult(DoNotShowAgain).send()
    }

    private fun Command.send() {
        viewModelScope.launch(dispatchers.io()) {
            _commands.send(this@send)
        }
    }
}
