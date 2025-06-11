

package com.duckduckgo.app.browser.omnibar

interface OmnibarBehaviour {

    fun measuredHeight(): Int

    fun height(): Int

    fun getTranslation(): Float

    fun setTranslation(y: Float)

    fun isOmnibarScrollingEnabled(): Boolean
}
