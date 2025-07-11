

package com.duckduckgo.autofill.sync

import android.content.Context
import android.view.View
import com.duckduckgo.sync.api.SyncMessagePlugin
import javax.inject.Inject

class CredentialsSyncPausedSyncMessagePlugin @Inject constructor() : SyncMessagePlugin {
    override fun getView(context: Context): View {
        return CredentialsSyncPausedView(context)
    }
}
