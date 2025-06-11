

package com.duckduckgo.app.fire

/** Public interface for FireproofRepository */
interface FireproofRepository {
    /**
     * This method returns a list of strings containing the domains that are fireproofed
     */
    fun fireproofWebsites(): List<String>
}
