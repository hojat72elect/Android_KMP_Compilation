package com.amaze.fileutilities.utilis

import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment

/**
 * General purpose fragment that supports show / hide system bars on top
 */
abstract class AbstractMediaFragment : Fragment() {

    companion object {
        const val ANIM_FADE = 500L
    }

    abstract fun getRootLayout(): View

    abstract fun getToolbarLayout(): View?

    abstract fun getBottomBarLayout(): View?

    /**
     * Do hide or show layouts
     * @param performActionOnSystemBars whether to perform action on status bar,
     * because that may cause screen ui to refresh
     */
    fun refactorSystemUi(hide: Boolean, performActionOnSystemBars: Boolean) {
        if (hide) {
            if (performActionOnSystemBars) {
                WindowInsetsControllerCompat(
                    requireActivity().window,
                    getRootLayout()
                ).let { controller ->
                    controller.hide(WindowInsetsCompat.Type.systemBars())
                    controller.systemBarsBehavior = WindowInsetsControllerCompat
                        .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
            getToolbarLayout()?.hideFade(ANIM_FADE)
            getBottomBarLayout()?.hideFade(ANIM_FADE)
        } else {
            if (performActionOnSystemBars) {
                WindowInsetsControllerCompat(
                    requireActivity().window,
                    getRootLayout()
                ).let { controller ->
                    controller.show(WindowInsetsCompat.Type.systemBars())
                    controller.systemBarsBehavior = WindowInsetsControllerCompat
                        .BEHAVIOR_SHOW_BARS_BY_TOUCH
                }
            }
            getToolbarLayout()?.showFade(ANIM_FADE)
            getBottomBarLayout()?.showFade(ANIM_FADE)
        }
    }
}
