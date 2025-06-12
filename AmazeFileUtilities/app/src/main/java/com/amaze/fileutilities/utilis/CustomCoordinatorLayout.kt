package com.amaze.fileutilities.utilis

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

class CustomCoordinatorLayout : CoordinatorLayout {
    private var proxyView: View? = null

    constructor(context: Context) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun isPointInChildBounds(
        child: View,
        x: Int,
        y: Int
    ): Boolean {
        if (super.isPointInChildBounds(child, x, y)) {
            return true
        }

        // we want to intercept touch events if they are
        // within the proxy view bounds, for this reason
        // we instruct the coordinator layout to check
        // if this is true and let the touch delegation
        // respond to that result
        return if (proxyView != null) {
            super.isPointInChildBounds(proxyView!!, x, y)
        } else false
    }

    // for this example we are only interested in intercepting
    // touch events for a single view, if more are needed use
    // a List<View> viewList instead and iterate in
    // isPointInChildBounds
    fun setProxyView(proxyView: View?) {
        this.proxyView = proxyView
    }
}
