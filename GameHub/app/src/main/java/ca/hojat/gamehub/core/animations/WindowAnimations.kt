package ca.hojat.gamehub.core.animations

import ca.hojat.gamehub.R

enum class WindowAnimations(
    val id: Int,
    val windowAEnterAnimation: Int = 0,
    val windowAExitAnimation: Int = 0,
    val windowBEnterAnimation: Int = 0,
    val windowBExitAnimation: Int = 0
) {
    HORIZONTAL_SLIDING_ANIMATIONS(
        id = 5,
        windowAEnterAnimation = R.anim.horizontal_sliding_window_a_enter_animation,
        windowAExitAnimation = R.anim.horizontal_sliding_window_a_exit_animation,
        windowBEnterAnimation = R.anim.horizontal_sliding_window_b_enter_animation,
        windowBExitAnimation = R.anim.horizontal_sliding_window_b_exit_animation
    )
}
