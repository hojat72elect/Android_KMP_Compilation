

package com.duckduckgo.sync.impl.ui

import android.content.Context
import android.view.View
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.sync.api.SyncMessagePlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(scope = ActivityScope::class)
class SyncErrorMessagePlugin @Inject constructor() : SyncMessagePlugin {
    override fun getView(context: Context): View {
        return SyncErrorView(context)
    }
}
