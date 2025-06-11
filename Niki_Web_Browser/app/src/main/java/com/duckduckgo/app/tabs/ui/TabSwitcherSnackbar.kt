

package com.duckduckgo.app.tabs.ui

import android.view.View
import android.widget.TextView
import com.google.android.material.R as materialR
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class TabSwitcherSnackbar(
    anchorView: View,
    message: String,
    private val action: String? = null,
    private val showAction: Boolean = false,
    private val onAction: () -> Unit = {},
    private val onDismiss: () -> Unit = {},
) {
    companion object {
        private const val SNACKBAR_DISPLAY_TIME_MS = 3500
    }

    private val snackbar = Snackbar.make(anchorView, message, Snackbar.LENGTH_LONG)
        .setDuration(SNACKBAR_DISPLAY_TIME_MS)
        .apply {
            if (showAction) {
                setAction(action) {
                    // noop, handled in onDismissed callback
                }
            }
        }
        .addCallback(
            object : Snackbar.Callback() {
                override fun onDismissed(
                    transientBottomBar: Snackbar?,
                    event: Int,
                ) {
                    when (event) {
                        // handle the UNDO action here as we only have one
                        BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_ACTION -> onAction()
                        BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_SWIPE,
                        BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_TIMEOUT,
                        -> onDismiss()
                        BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_CONSECUTIVE,
                        BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_MANUAL,
                        -> { /* noop */ }
                    }
                }
            },
        )
        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
        .apply { view.findViewById<TextView>(materialR.id.snackbar_text).maxLines = 1 }

    fun show() {
        snackbar.show()
    }
}
