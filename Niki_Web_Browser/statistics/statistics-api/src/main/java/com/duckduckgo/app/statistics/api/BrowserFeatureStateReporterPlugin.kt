

package com.duckduckgo.app.statistics.api

interface BrowserFeatureStateReporterPlugin {

    /**
     * Used to report the state across different modules in the browser
     * @return a map of key-value pairs that represent the state of the features
     */
    fun featureStateParams(): Map<String, String>
}
