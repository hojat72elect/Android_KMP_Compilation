

package com.duckduckgo.savedsites.impl.sync

import android.content.Context
import android.view.View
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.sync.api.SyncMessagePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(scope = ActivityScope::class)
class SavedSitesInvalidItemSyncMessagePlugin @Inject constructor() : SyncMessagePlugin {
    override fun getView(context: Context): View {
        return SavedSiteInvalidItemsView(context)
    }
}
