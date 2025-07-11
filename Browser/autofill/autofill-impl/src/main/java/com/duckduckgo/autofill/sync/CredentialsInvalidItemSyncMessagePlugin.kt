

package com.duckduckgo.autofill.sync

import android.content.Context
import android.view.View
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.sync.api.SyncMessagePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(scope = ActivityScope::class)
class CredentialsInvalidItemSyncMessagePlugin @Inject constructor() : SyncMessagePlugin {
    override fun getView(context: Context): View {
        return CredentialsInvalidItemsView(context)
    }
}
