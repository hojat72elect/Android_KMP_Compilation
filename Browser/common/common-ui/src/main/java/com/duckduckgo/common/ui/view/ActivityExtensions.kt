

package com.duckduckgo.common.ui.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity

fun Context.fadeTransitionConfig(): Bundle? {
    val config = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
    return config.toBundle()
}

fun Context.noAnimationConfig(): Bundle? =
    ActivityOptionsCompat.makeCustomAnimation(this, 0, 0).toBundle()

fun FragmentActivity.isFullScreen(): Boolean {
    return window.decorView.systemUiVisibility.and(View.SYSTEM_UI_FLAG_FULLSCREEN) == View.SYSTEM_UI_FLAG_FULLSCREEN
}

fun FragmentActivity.isImmersiveModeEnabled(): Boolean {
    val uiOptions = window.decorView.systemUiVisibility
    return uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions
}
