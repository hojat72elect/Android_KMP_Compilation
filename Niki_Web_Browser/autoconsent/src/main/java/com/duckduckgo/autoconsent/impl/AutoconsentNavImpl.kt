package com.duckduckgo.autoconsent.impl

import android.content.Context
import android.content.Intent
import com.duckduckgo.autoconsent.api.AutoconsentNav
import com.duckduckgo.autoconsent.impl.ui.AutoconsentSettingsActivity
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class AutoconsentNavImpl @Inject constructor() : AutoconsentNav {
    override fun openAutoconsentSettings(context: Context): Intent {
        return AutoconsentSettingsActivity.intent(context)
    }
}
