

package com.duckduckgo.app.browser.webview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
import androidx.core.view.isGone
import com.duckduckgo.app.browser.navigation.bar.view.BrowserNavigationBarView
import com.duckduckgo.app.browser.omnibar.OmnibarLayout
import com.duckduckgo.app.browser.omnibar.model.OmnibarPosition
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.ScrollingViewBehavior

/**
 * A [ScrollingViewBehavior] that observes [AppBarLayout] (top omnibar) present in the view hierarchy and applies top offset to the child view
 * equal to the visible height of the omnibar.
 *
 * This extension additionally observes the position of [BrowserNavigationBarView], if present and a sibling of the target child in the [CoordinatorLayout],
 * and applies bottom padding to the target child equal to the visible height of the navigation bar.
 *
 * This prevents the omnibar and the navigation bar from overlapping with, for example, content found in the web view.
 *
 * Note: If bottom [OmnibarLayout] is used ([OmnibarPosition.BOTTOM]), [BottomOmnibarBrowserContainerLayoutBehavior] should be set to the target child.
 */
class TopOmnibarBrowserContainerLayoutBehavior(
    context: Context,
    attrs: AttributeSet?,
) : ScrollingViewBehavior(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View,
    ): Boolean {
        return dependency.isBrowserNavigationBar() || super.layoutDependsOn(parent, child, dependency)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View,
    ): Boolean {
        return if (dependency.isBrowserNavigationBar()) {
            offsetByBottomElementVisibleHeight(child = child, dependency = dependency)
        } else {
            super.onDependentViewChanged(parent, child, dependency)
        }
    }
}

/**
 * A behavior that observes the position of the bottom [OmnibarLayout] ([OmnibarPosition.BOTTOM]), if present,
 * and applies bottom padding to the target view equal to the visible height of the omnibar.
 *
 * This prevents the omnibar from overlapping with, for example, content found in the web view.
 *
 * We can't use the [ScrollingViewBehavior] because it relies on the [AppBarLayout] and always forcefully places the target child _below_ the bar,
 * which doesn't work if the bar is at the bottom.
 *
 * We don't need to additionally observe the position of the [BrowserNavigationBarView] when bottom [OmnibarLayout] is used because it comes pre-embedded with the navigation bar.
 *
 * Note: If top [OmnibarLayout] is used ([OmnibarPosition.TOP]), [TopOmnibarBrowserContainerLayoutBehavior] should be set to the target child.
 */
class BottomOmnibarBrowserContainerLayoutBehavior : Behavior<View>() {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View,
    ): Boolean {
        return dependency.isBottomOmnibar() || super.layoutDependsOn(parent, child, dependency)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View,
    ): Boolean {
        return if (dependency.isBottomOmnibar()) {
            offsetByBottomElementVisibleHeight(child = child, dependency = dependency)
        } else {
            super.onDependentViewChanged(parent, child, dependency)
        }
    }
}

private fun offsetByBottomElementVisibleHeight(
    child: View,
    dependency: View,
): Boolean {
    val newBottomPadding = if (dependency.isGone) {
        0
    } else {
        dependency.measuredHeight - dependency.translationY.toInt()
    }
    return if (child.paddingBottom != newBottomPadding) {
        child.setPadding(
            0,
            0,
            0,
            newBottomPadding,
        )
        true
    } else {
        false
    }
}

private fun View.isBrowserNavigationBar(): Boolean = this is BrowserNavigationBarView
private fun View.isBottomOmnibar(): Boolean = this is OmnibarLayout && this.omnibarPosition == OmnibarPosition.BOTTOM
