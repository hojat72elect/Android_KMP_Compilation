

package com.duckduckgo.app.downloads

import com.duckduckgo.navigation.api.GlobalActivityStarter.ActivityParams

sealed interface DownloadsScreens {

    /**
     * Launch the Downloads activity
     */
    object DownloadsScreenNoParams : ActivityParams {
        private fun readResolve(): Any = DownloadsScreenNoParams
    }
}
