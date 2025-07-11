

package com.duckduckgo.common.ui.view.dialog

import android.content.DialogInterface
import android.view.KeyEvent

class BackKeyListener(private val onBackPressed: () -> Unit) : DialogInterface.OnKeyListener {

    override fun onKey(
        dialog: DialogInterface?,
        keyCode: Int,
        event: KeyEvent?,
    ): Boolean {
        if (isBackKey(keyCode, event)) {
            onBackPressed.invoke()
            dialog?.dismiss()
            return true
        }
        return false
    }

    private fun isBackKey(
        keyCode: Int,
        event: KeyEvent?,
    ): Boolean {
        return (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_UP)
    }
}
