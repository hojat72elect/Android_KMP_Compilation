

package com.duckduckgo.common.ui.internal.experiments

import android.content.Context
import android.view.View

interface ExperimentalUIPlugin {

    /**
     * This method returns a [View] that will be used as the plugin content
     * @return [View]
     */
    fun getView(context: Context): View
}
