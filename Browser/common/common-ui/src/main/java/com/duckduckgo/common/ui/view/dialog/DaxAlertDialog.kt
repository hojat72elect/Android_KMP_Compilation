

package com.duckduckgo.common.ui.view.dialog

interface DaxAlertDialog {

    fun build(): DaxAlertDialog
    fun show()
    fun dismiss()
    fun isShowing(): Boolean
}
