

package com.duckduckgo.app.browser.omnibar.animations

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.duckduckgo.app.trackerdetection.model.Entity
import com.duckduckgo.common.ui.view.text.DaxTextView

/** Public interface for the Browser URL Bar Privacy and Trackers animations */
interface BrowserTrackersAnimatorHelper {

    /**
     * This method takes [entities] to create an animation in [trackersAnimationView].
     * Then it plays both animations, [shieldAnimationView] and [trackersAnimationView], at the same time.
     * When the animations starts, views in [omnibarViews] will fade out. When animation finishes, view in [omnibarViews] will fade in.
     *
     * @param shieldAnimationView holder of the privacy shield animation.
     * @param trackersAnimationView holder of the trackers animations.
     * @param omnibarViews are the views that should be hidden while the animation is running
     * @param entities are the tracker entities detected on the current site
     * @param visualDesignExperimentEnabled if the visual design experiment is enabled
     */
    fun startTrackersAnimation(
        context: Context,
        shieldAnimationView: LottieAnimationView,
        trackersAnimationView: LottieAnimationView,
        omnibarViews: List<View>,
        entities: List<Entity>?,
        visualDesignExperimentEnabled: Boolean = false,
    )

    fun startExperimentVariant1Animation(
        context: Context,
        shieldAnimationView: LottieAnimationView,
        omnibarViews: List<View>,
    )

    fun startExperimentVariant2OrVariant3Animation(
        context: Context,
        shieldAnimationView: LottieAnimationView,
        trackersBlockedAnimationView: DaxTextView,
        trackersBlockedCountAnimationView: DaxTextView,
        omnibarViews: List<View>,
        shieldViews: List<View>,
        entities: List<Entity>?,
    )

    /**
     * This method creates or enqueues cookie consent animation.
     *
     * @param omnibarViews are the views that should be hidden while the animation is running.
     * @param cookieBackground holder of the cookie consent animation background.
     * @param cookieAnimationView holder of the cookie consent animation.
     * @param cookieScene holder of cookie consent text animation.
     */
    fun createCookiesAnimation(
        context: Context,
        omnibarViews: List<View>,
        shieldViews: List<View>,
        cookieBackground: View,
        cookieAnimationView: LottieAnimationView,
        cookieScene: ViewGroup,
        cookieCosmeticHide: Boolean,
        enqueueCookieAnimation: Boolean,
    )

    /**
     * Cancel a running animation.
     *
     * @param omnibarViews are the views that should become visible after canceling the running animation.
     */
    fun cancelAnimations(
        omnibarViews: List<View>,
    )

    /**
     * Set [TrackersAnimatorListener] to receive animation progress events.
     */
    fun setListener(animatorListener: TrackersAnimatorListener)

    /**
     * removes [TrackersAnimatorListener]
     */
    fun removeListener()
}

/**
 * A TrackersAnimatorListener receives animation related events,
 * such as the end of the animation.
 */
interface TrackersAnimatorListener {

    /**
     * Notifies the end of the animation.
     * The callback is not be invoked when a partial animation is paused.
     */
    fun onAnimationFinished()
}
