

package com.duckduckgo.savedsites.impl.sync

import android.content.*
import android.view.*
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.di.scopes.*
import com.duckduckgo.sync.api.*
import com.squareup.anvil.annotations.*
import javax.inject.*

@ContributesMultibinding(scope = ActivityScope::class)
@PriorityKey(100)
class DisplayModeSyncSettingsPlugin @Inject constructor() : SyncSettingsPlugin {
    override fun getView(context: Context): View {
        return DisplayModeSyncSetting(context)
    }
}
