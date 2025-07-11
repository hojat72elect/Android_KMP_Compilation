

package com.duckduckgo.sync.api

import com.duckduckgo.navigation.api.GlobalActivityStarter

/**
 * Use this class to launch the sync screen without parameters
 * ```kotlin
 * globalActivityStarter.start(context, SyncActivityWithEmptyParams)
 * ```
 */
object SyncActivityWithEmptyParams : GlobalActivityStarter.ActivityParams
