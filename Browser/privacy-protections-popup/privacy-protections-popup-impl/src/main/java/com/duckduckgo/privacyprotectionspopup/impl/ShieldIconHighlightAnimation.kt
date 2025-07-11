

package com.duckduckgo.privacyprotectionspopup.impl

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation

fun buildShieldIconHighlightAnimation(): Animation {
    val fadeInAnimation = AlphaAnimation(0.0f, 1.0f).apply {
        duration = 500
        startOffset = 500
    }

    val scaleAnimation = ScaleAnimation(
        1.0f, // Start scale for X
        1.2f, // End scale for X
        1.0f, // Start scale for Y
        1.2f, // End scale for Y
        Animation.RELATIVE_TO_SELF,
        0.5f, // Pivot X at the center
        Animation.RELATIVE_TO_SELF,
        0.5f, // Pivot Y at the center
    ).apply {
        duration = 800
        repeatCount = Animation.INFINITE
        repeatMode = Animation.REVERSE
    }

    return AnimationSet(false).apply {
        addAnimation(fadeInAnimation)
        addAnimation(scaleAnimation)
    }
}
