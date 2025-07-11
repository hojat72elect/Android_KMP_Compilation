

package com.duckduckgo.sync.api

import android.content.Context
import android.view.View

interface SyncMessagePlugin {
    fun getView(context: Context): View
}
