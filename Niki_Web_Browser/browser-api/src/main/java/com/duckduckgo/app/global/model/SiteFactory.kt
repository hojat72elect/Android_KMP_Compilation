

package com.duckduckgo.app.global.model

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread

interface SiteFactory {
    @AnyThread
    fun buildSite(
        url: String,
        tabId: String,
        title: String? = null,
        httpUpgraded: Boolean = false,
        externalLaunch: Boolean = false,
    ): Site

    /**
     * Updates the given Site with the full details
     *
     * This can be expensive to execute.
     */
    @WorkerThread
    fun loadFullSiteDetails(site: Site)
}
