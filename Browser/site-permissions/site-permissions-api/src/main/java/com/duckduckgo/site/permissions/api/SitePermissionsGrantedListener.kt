

package com.duckduckgo.site.permissions.api

/** Public listener interface for implementing special cases when websites need extra logic
 * when requesting permissions */
interface SitePermissionsGrantedListener {

    /** Loads embedded room url when permission is granted */
    fun permissionsGrantedOnWhereby()
}
