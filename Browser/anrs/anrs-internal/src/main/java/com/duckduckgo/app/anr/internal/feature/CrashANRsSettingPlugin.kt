

package com.duckduckgo.app.anr.internal.feature

import android.content.*
import android.view.*
import com.duckduckgo.anvil.annotations.ContributesPluginPoint
import com.duckduckgo.di.scopes.ActivityScope

/**
 * Use this interface to create a new plugin that will be used to display a specific settings section
 */
@ContributesPluginPoint(ActivityScope::class)
interface CrashANRsSettingPlugin {
    /**
     * This method returns a [View] that will be used as a setting item
     * @return [View]
     */
    fun getView(context: Context): View?
}
