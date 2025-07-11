

package com.duckduckgo.networkprotection.impl.subscription.settings

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.settings.api.ProSettingsPlugin
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(ActivityScope::class)
@PriorityKey(200)
class ProSettingsNetP @Inject constructor() : ProSettingsPlugin {
    override fun getView(context: Context): View {
        return ProSettingNetPView(context)
    }
}
