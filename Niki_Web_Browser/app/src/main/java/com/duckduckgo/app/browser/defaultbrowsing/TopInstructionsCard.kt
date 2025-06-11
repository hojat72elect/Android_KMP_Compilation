

package com.duckduckgo.app.browser.defaultbrowsing

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.duckduckgo.app.browser.R

class TopInstructionsCard(
    context: Context,
    duration: Int = LENGTH_LONG,
) : Toast(context) {

    init {
        val inflater = LayoutInflater.from(context)
        val inflatedView = inflater.inflate(R.layout.content_onboarding_default_browser_card, null)
        view = inflatedView
        setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0)
        this.duration = duration
    }
}
