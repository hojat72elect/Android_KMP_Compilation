

package com.duckduckgo.common.ui.store.notifyme

interface NotifyMeDataStore {

    fun isComponentDismissed(key: String, defaultValue: Boolean): Boolean

    fun setComponentDismissed(key: String)
}
