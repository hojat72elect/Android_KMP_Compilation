

package com.duckduckgo.verifiedinstallation

/**
 * Returns whether the app is a verified build, installed from the Play Store
 */
interface IsVerifiedPlayStoreInstall {
    operator fun invoke(): Boolean
}
