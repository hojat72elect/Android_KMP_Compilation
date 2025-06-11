package com.duckduckgo.customtabs.impl

import com.duckduckgo.customtabs.api.CustomTabDetector
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesBinding
import dagger.SingleInstanceIn
import javax.inject.Inject

@ContributesBinding(scope = AppScope::class)
@SingleInstanceIn(AppScope::class)
class RealCustomTabDetector @Inject constructor() : CustomTabDetector {

    private var customTab: Boolean = false

    override fun isCustomTab(): Boolean {
        return customTab
    }

    override fun setCustomTab(value: Boolean) {
        this.customTab = value
    }
}
