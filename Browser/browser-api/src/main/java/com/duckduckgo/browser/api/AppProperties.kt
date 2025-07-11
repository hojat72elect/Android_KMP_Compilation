

package com.duckduckgo.browser.api

interface AppProperties {
    fun atb(): String
    fun appAtb(): String
    fun searchAtb(): String
    fun expVariant(): String
    fun installedGPlay(): Boolean
    fun webView(): String
}
