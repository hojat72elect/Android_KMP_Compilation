

package com.duckduckgo.app.browser.defaultbrowsing

interface DefaultBrowserDetector {
    fun deviceSupportsDefaultBrowserConfiguration(): Boolean
    fun isDefaultBrowser(): Boolean
    fun hasDefaultBrowser(): Boolean
}
