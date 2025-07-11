

package com.duckduckgo.app.global.view

import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar

class NonDismissibleBehavior : BaseTransientBottomBar.Behavior() {
    override fun canSwipeDismissView(child: View) = false
}
