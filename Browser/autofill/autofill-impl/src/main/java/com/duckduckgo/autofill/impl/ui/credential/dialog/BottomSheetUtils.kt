

package com.duckduckgo.autofill.impl.ui.credential.dialog

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun BottomSheetDialog.animateClosed() {
    behavior.state = BottomSheetBehavior.STATE_HIDDEN
}
