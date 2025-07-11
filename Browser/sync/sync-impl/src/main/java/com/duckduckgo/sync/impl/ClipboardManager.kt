

package com.duckduckgo.sync.impl

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.duckduckgo.di.scopes.ActivityScope
import com.squareup.anvil.annotations.ContributesBinding
import java.lang.*
import javax.inject.*

interface Clipboard {
    fun copyToClipboard(toCopy: String)
    fun pasteFromClipboard(): String
}

@ContributesBinding(ActivityScope::class)
class RealClipboard @Inject constructor(
    context: Context,
) : Clipboard {

    private val clipboardManager by lazy { context.getSystemService(ClipboardManager::class.java) }

    override fun copyToClipboard(toCopy: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", toCopy))
    }

    override fun pasteFromClipboard(): String {
        return if (clipboardManager.hasPrimaryClip()) {
            clipboardManager.primaryClip?.getItemAt(0)?.text.takeUnless { it.isNullOrBlank() }?.toString() ?: ""
        } else {
            ""
        }
    }
}
