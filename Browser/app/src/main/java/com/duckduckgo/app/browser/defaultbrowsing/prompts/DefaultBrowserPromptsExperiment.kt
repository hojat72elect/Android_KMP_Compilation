

package com.duckduckgo.app.browser.defaultbrowsing.prompts

import android.content.Intent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DefaultBrowserPromptsExperiment {

    val highlightPopupMenu: StateFlow<Boolean>
    val showSetAsDefaultPopupMenuItem: StateFlow<Boolean>
    val commands: Flow<Command>

    fun onPopupMenuLaunched()
    fun onSetAsDefaultPopupMenuItemSelected()

    fun onMessageDialogShown()
    fun onMessageDialogCanceled()
    fun onMessageDialogConfirmationButtonClicked()
    fun onMessageDialogNotNowButtonClicked()

    fun onSystemDefaultBrowserDialogShown()
    fun onSystemDefaultBrowserDialogSuccess(trigger: SetAsDefaultActionTrigger)
    fun onSystemDefaultBrowserDialogCanceled(trigger: SetAsDefaultActionTrigger)

    fun onSystemDefaultAppsActivityClosed(trigger: SetAsDefaultActionTrigger)

    sealed class Command {
        data object OpenMessageDialog : Command()
        data class OpenSystemDefaultBrowserDialog(
            val intent: Intent,
            val trigger: SetAsDefaultActionTrigger,
        ) : Command()

        data class OpenSystemDefaultAppsActivity(
            val intent: Intent,
            val trigger: SetAsDefaultActionTrigger,
        ) : Command()
    }

    enum class SetAsDefaultActionTrigger {
        DIALOG,
        MENU,
        UNKNOWN,
    }
}
