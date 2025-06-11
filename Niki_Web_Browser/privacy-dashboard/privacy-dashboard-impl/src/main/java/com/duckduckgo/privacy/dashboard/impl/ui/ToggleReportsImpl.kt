

package com.duckduckgo.privacy.dashboard.impl.ui

import com.duckduckgo.di.scopes.AppScope
import com.duckduckgo.privacy.dashboard.api.ui.ToggleReports
import com.duckduckgo.privacy.dashboard.impl.ToggleReportsDataStore
import com.duckduckgo.privacy.dashboard.impl.ToggleReportsFeature
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class ToggleReportsImpl @Inject constructor(
    private val toggleReportsFeature: ToggleReportsFeature,
    private val toggleReportsDataStore: ToggleReportsDataStore,
) : ToggleReports {
    override suspend fun shouldPrompt(): Boolean {
        return (toggleReportsFeature.self().isEnabled() && toggleReportsDataStore.canPrompt())
    }

    override suspend fun onPromptDismissed() {
        toggleReportsDataStore.insertTogglePromptDismiss()
    }

    override suspend fun onReportSent() {
        toggleReportsDataStore.insertTogglePromptSend()
    }
}
