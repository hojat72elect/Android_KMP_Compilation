

package com.duckduckgo.subscriptions.impl.settings.plugins

import android.content.Context
import android.view.View
import com.duckduckgo.anvil.annotations.PriorityKey
import com.duckduckgo.common.ui.view.listitem.SectionHeaderListItem
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.settings.api.ProSettingsPlugin
import com.duckduckgo.subscriptions.impl.R
import com.duckduckgo.subscriptions.impl.settings.views.ItrSettingView
import com.duckduckgo.subscriptions.impl.settings.views.PirSettingView
import com.duckduckgo.subscriptions.impl.settings.views.ProSettingView
import com.squareup.anvil.annotations.ContributesMultibinding
import javax.inject.Inject

@ContributesMultibinding(ActivityScope::class)
@PriorityKey(100)
class ProSettingsTitle @Inject constructor() : ProSettingsPlugin {
    override fun getView(context: Context): View {
        return SectionHeaderListItem(context).apply {
            primaryText = context.getString(R.string.privacyPro)
        }
    }
}

@ContributesMultibinding(scope = ActivityScope::class)
@PriorityKey(500)
class ProSettings @Inject constructor() : ProSettingsPlugin {
    override fun getView(context: Context): View {
        return ProSettingView(context)
    }
}

@ContributesMultibinding(scope = ActivityScope::class)
@PriorityKey(300)
class PIRSettings @Inject constructor() : ProSettingsPlugin {
    override fun getView(context: Context): View {
        return PirSettingView(context)
    }
}

@ContributesMultibinding(scope = ActivityScope::class)
@PriorityKey(400)
class ITRSettings @Inject constructor() : ProSettingsPlugin {
    override fun getView(context: Context): View {
        return ItrSettingView(context)
    }
}
