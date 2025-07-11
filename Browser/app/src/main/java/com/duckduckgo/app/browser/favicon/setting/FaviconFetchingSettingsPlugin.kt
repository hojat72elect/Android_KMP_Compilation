

package com.duckduckgo.app.browser.favicon.setting

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.sync.api.SyncSettingsPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(scope = ActivityScope::class)
@PriorityKey(200)
class FaviconFetchingSettingsPlugin @Inject constructor() : SyncSettingsPlugin {
    override fun getView(context: Context): View {
        return FaviconFetchingSyncSetting(context)
    }
}
