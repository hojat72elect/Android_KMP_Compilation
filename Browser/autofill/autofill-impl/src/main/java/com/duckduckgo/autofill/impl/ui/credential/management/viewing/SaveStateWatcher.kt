

package com.duckduckgo.autofill.impl.ui.credential.management.viewing

import android.text.Editable
import android.text.TextWatcher
import com.duckduckgo.autofill.impl.databinding.FragmentAutofillManagementEditModeBinding
import com.duckduckgo.autofill.impl.ui.credential.management.viewing.SaveStateWatcher.TextState
import javax.inject.Inject

fun FragmentAutofillManagementEditModeBinding.currentTextState(): TextState {
    return TextState(
        title = domainTitleEditText.text,
        username = usernameEditText.text,
        password = passwordEditText.text,
        websiteUrl = domainEditText.text,
        notes = notesEditText.text,
    )
}

fun FragmentAutofillManagementEditModeBinding.watchSaveState(
    saveStateWatcher: SaveStateWatcher,
    saveStateUpdater: () -> Unit = {},
) {
    saveStateWatcher.start {
        saveStateUpdater()
    }
    domainTitleEditText.addTextChangedListener(saveStateWatcher)
    domainEditText.addTextChangedListener(saveStateWatcher)
    notesEditText.addTextChangedListener(saveStateWatcher)
    usernameEditText.addTextChangedListener(saveStateWatcher)
    passwordEditText.addTextChangedListener(saveStateWatcher)
}

fun FragmentAutofillManagementEditModeBinding.removeSaveStateWatcher(saveStateWatcher: SaveStateWatcher) {
    domainTitleEditText.removeTextChangedListener(saveStateWatcher)
    domainEditText.removeTextChangedListener(saveStateWatcher)
    notesEditText.removeTextChangedListener(saveStateWatcher)
    usernameEditText.removeTextChangedListener(saveStateWatcher)
    passwordEditText.removeTextChangedListener(saveStateWatcher)
}

class SaveStateWatcher @Inject constructor() : TextWatcher {
    private var _saveStateUpdater: () -> Unit = {}
    fun start(
        saveStateUpdater: () -> Unit,
    ) {
        _saveStateUpdater = saveStateUpdater
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int,
    ) {
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int,
    ) {
    }

    override fun afterTextChanged(s: Editable?) {
        _saveStateUpdater()
    }

    data class TextState(
        val title: String,
        val username: String,
        val password: String,
        val websiteUrl: String,
        val notes: String,
    ) {
        fun isEmpty(): Boolean {
            return title.isBlank() && username.isBlank() && password.isBlank() && websiteUrl.isBlank() && notes.isBlank()
        }

        override fun toString(): String {
            return "TextState(" +
                "title='$title', " +
                "username='$username', " +
                "password='********', " +
                "websiteUrl='$websiteUrl', " +
                "notes='$notes')"
        }
    }
}
