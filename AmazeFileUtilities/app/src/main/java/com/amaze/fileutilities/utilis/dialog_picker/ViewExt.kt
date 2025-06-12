package com.amaze.fileutilities.utilis.dialog_picker

import android.view.View

internal fun <T : View> T.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}
