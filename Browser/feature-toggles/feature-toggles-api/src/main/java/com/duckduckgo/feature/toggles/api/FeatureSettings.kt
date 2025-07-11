
package com.duckduckgo.feature.toggles.api

@Deprecated(message = "Not needed anymore. Settings is now supported in top-leve and sub-features and Toggle#getSettings returns it")
object FeatureSettings {
    @Deprecated(message = "Not needed anymore. Settings is now supported in top-leve and sub-features and Toggle#getSettings returns it")
    interface Store {
        fun store(
            jsonString: String,
        )
    }

    val EMPTY_STORE = object : Store {
        override fun store(jsonString: String) {}
    }
}
